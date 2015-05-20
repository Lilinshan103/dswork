package dswork.android.demo.component.downloadlist;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dswork.android.demo.component.downloadlist.model.FileInfo;
import dswork.android.demo.component.downloadlist.model.ThreadInfo;
import dswork.android.demo.component.downloadlist.service.ThreadInfoService;

/**
 * Created by ole on 15/5/8.
 */
public class DownloadTask
{
    private Context ctx = null;
    private FileInfo mFileInfo = null;
    private ThreadInfoService service = null;
    private long mFinished = 0;
    private int mThreadCount = 1;//线程数量
    private List<DownloadThread> mThreadList = null;

    public static ExecutorService sExecutorService = Executors.newCachedThreadPool();//线程池
    public boolean isPause = false;


    public DownloadTask(Context ctx, FileInfo mFileInfo, int mThreadCount)
    {
        this.ctx = ctx;
        this.mFileInfo = mFileInfo;
        this.mThreadCount = mThreadCount;
        this.service = new ThreadInfoService(ctx);
    }

    public void download()
    {
        //读取数据库的线程信息
        Map m = new HashMap();
        m.put("url", mFileInfo.getUrl());
        List<ThreadInfo> threadInfos = service.query(m);

        if(threadInfos.size() == 0)
        {
            //获得每个线程下载长度
            int length = mFileInfo.getLengthInt()/mThreadCount;
            for(int i=0; i<mThreadCount; i++)
            {
                //创建线程信息
                ThreadInfo mThreadInfo = new ThreadInfo(i, mFileInfo.getUrl(), length*i, (i+1)*length-1, 0);
                if(i == mThreadCount-1)
                {
                    mThreadInfo.setEnd(mFileInfo.getLength());
                }
                //添加线程信息到集合中
                threadInfos.add(mThreadInfo);
                //插入下载线程
                service.add("thread_info", mThreadInfo);
            }
        }
        mThreadList = new ArrayList<DownloadThread>();
        //启动多个线程进行下载
        for(ThreadInfo po : threadInfos)
        {
            DownloadThread mDownloadThread = new DownloadThread(po);
//            mDownloadThread.start();
            DownloadTask.sExecutorService.execute(mDownloadThread);
            mThreadList.add(mDownloadThread);
        }
    }

    /**
     * 判断是否所有线程都执行完毕
     */
    private synchronized void checkAllThreadsFinished()
    {
        boolean allFinished = true;
        for(DownloadThread thread : mThreadList)
        {
            if (!thread.isFinished)
            {
                allFinished = false;
                break;
            }
        }
        if(allFinished)
        {

            //删除线程信息
            service.deleteByUrl(mFileInfo.getUrl());
            //发送广播通知U下载任务结束
            Intent intent = new Intent(DownloadService.ACTION_FINISH);
            intent.putExtra("fileinfo",mFileInfo);
            ctx.sendBroadcast(intent);
        }
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread
    {
        private ThreadInfo mThreadInfo = null;
        public boolean isFinished = false;//线程是否执行完毕

        public DownloadThread(ThreadInfo mThreadInfo)
        {
            this.mThreadInfo = mThreadInfo;
        }

        public void run()
        {
            //向数据库插入线程信息
            Map<String,Object> m = new HashMap<String,Object>();
            m.put("id",mThreadInfo.getId());
            m.put("url", mThreadInfo.getUrl());
            Log.i("test","isExists:"+service.isExists(m));
            if(!service.isExists(m))
            {
                service.add("thread_info",mThreadInfo);
            }
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream input = null;
            try
            {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                long start = mThreadInfo.getStart() + mThreadInfo.getFinished();
                conn.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());
                Log.i("test", "Range:"+conn.getRequestProperty("Range"));
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH, mFileInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");
                //在读写的时候跳过设置好的字节数，从下一个字节数开始读写
                //例如：seek(100),则跳过100个字节，从第101个字节开始读写
                raf.seek(start);
                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                mFinished += mThreadInfo.getFinished();
                //开始下载
                if(conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT)
                {
                    //读取数据
                    input = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while((len = input.read(buffer)) != -1)
                    {
                        //写入文件
                        raf.write(buffer,0,len);
                        //把下载进度发送广播给Activity, 每隔500毫秒发送一次广播
                        mFinished += len;
                        //累加每个线程完成到进度
                        mThreadInfo.setFinished(mThreadInfo.getFinished()+len);
                        //间隔500毫秒更新一次进度
                        if(System.currentTimeMillis() - time > 500)
                        {
                            long _finished = mFinished * 100 / mFileInfo.getLength();
                            System.out.println("更新前："+String.valueOf(_finished));
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            intent.putExtra("id", mFileInfo.getId());
                            ctx.sendBroadcast(intent);
                        }
                        //在下载暂停时，保存下载进度
                        if(isPause)
                        {
                            mThreadInfo.setFinished(mFinished);
                            Log.i("test", "暂停："+mThreadInfo.toString());
                            service.updateFinished(mThreadInfo);
                            return;
                        }
                    }
                    //标识线程执行完毕
                    isFinished = true;
                    //检测下载任务是否执行完毕
                    checkAllThreadsFinished();

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    conn.disconnect();
                    raf.close();
                    input.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
