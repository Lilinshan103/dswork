package dswork.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.*;

public class MyRequestUpload
{
	private long totalMaxFileSize = 0L;
	private long maxFileSize = 0L;
	private int m_totalBytes = 0;
	private int m_currentIndex = 0;
	private int m_startData = 0;
	private int m_endData = 0;
	private String m_boundary = "";
	private String m_deniedFilesList = null;
	private String m_allowedFilesList = null;
	private byte[] m_binArray;
	private HttpServletRequest request = null;
	private Map<String, ArrayList<MyFile>> formFiles;
	private Map<String, ArrayList<String>> formParams;

	public Map<String, ArrayList<MyFile>> getFiles()
	{
		return formFiles;
	}

	public Map<String, ArrayList<String>> getParams()
	{
		return formParams;
	}

	public MyRequestUpload(HttpServletRequest httpservletrequest)
	{
		request = httpservletrequest;
		formFiles = new LinkedHashMap<String, ArrayList<MyFile>>();
		formParams = new LinkedHashMap<String, ArrayList<String>>();
	}

	public void uploadStream() throws Exception
	{
		String header = request.getHeader("Content-Disposition");
		boolean flagFileName = header.indexOf("filename") != -1;
		MyFile file = MyRequestUpload.getFileForHeader(header);
		if(flagFileName && (m_deniedFilesList != null || m_allowedFilesList != null))
		{
			checkFileExt(file);
		}
		int size = request.getContentLength();// 因为只有一个文件，所以判断单个大小即可
		checkFileSize(file, (long) (size));
		byte[] byteArray = new byte[size];
		int j = 0, k = 0;
		while(j < size && (k = request.getInputStream().read(byteArray, j, size - j)) != -1)
		{
			j += k;
		}
		file.setFileData(byteArray);
		String filedName = file.getFieldName();
		if(formFiles.get(filedName) == null)
		{
			formFiles.put(filedName, new ArrayList<MyFile>());
		}
		formFiles.get(filedName).add(file);
	}

	public void uploadForm() throws Exception
	{
		int i = 0;
		long l = 0L;
		boolean flag1 = false;
		m_totalBytes = request.getContentLength();
		m_binArray = new byte[m_totalBytes];
		int j;
		for(; i < m_totalBytes; i += j)
		{
			try
			{
				j = request.getInputStream().read(m_binArray, i, m_totalBytes - i);
				if(j == -1)
				{
					break;
				}
			}
			catch(Exception exception)
			{
				throw new Exception("MyRequestException:Request流接收异常");
			}
		}
		for(; !flag1 && m_currentIndex < m_totalBytes; m_currentIndex++)
		{
			if(m_binArray[m_currentIndex] == 13)
			{
				flag1 = true;
			}
			else
			{
				m_boundary = m_boundary + (char) m_binArray[m_currentIndex];
			}
		}
		if(m_currentIndex == 1)
		{
			return;
		}
		for(m_currentIndex++; m_currentIndex < m_totalBytes; m_currentIndex = m_currentIndex + 2)
		{
			String header = getDataHeader();
			boolean flagFileName = header.indexOf("filename") != -1;
			String filedName = getDataFieldValue(header, "name");// 字段名称
			m_currentIndex = m_currentIndex + 2;
			getDataSection();
			if(flagFileName)
			{
				MyFile file = getFileForHeader(header);
				int size = (m_endData - m_startData) + 1;// 单个文件大小
				if(size > 0 || file.getFileName().length() > 0)
				{
					if(m_deniedFilesList != null || m_allowedFilesList != null)
					{
						checkFileExt(file);
					}
					checkFileSize(file, (long) (size));
					l += (m_endData - m_startData) + 1;
					if(totalMaxFileSize > 0L && l > totalMaxFileSize)
					{
						throw new Exception("MyRequestException:所有文件大小超出范围");
					}
					if(file.getContentType().indexOf("application/x-macbinary") != -1)
					{
						m_startData = m_startData + 128;
					}
					file.setFileData(new byte[size]);
					System.arraycopy(m_binArray, m_startData, file.getFileData(), 0, size);
					if(formFiles.get(filedName) == null)
					{
						formFiles.put(filedName, new ArrayList<MyFile>());
					}
					formFiles.get(filedName).add(file);
				}
			}
			else
			{
				if(formParams.get(filedName) == null)
				{
					formParams.put(filedName, new ArrayList<String>());
				}
				formParams.get(filedName).add(new String(m_binArray, m_startData, (m_endData - m_startData) + 1, "UTF-8"));
			}
			if((char) m_binArray[m_currentIndex + 1] == '-')
			{
				break;
			}
		}
		m_binArray = null;
	}

	public void setDeniedFilesList(String s)
	{
		if(s != null)
		{
			m_deniedFilesList = "," + s.replaceAll(" ", "").toLowerCase() + ",";
		}
		else
		{
			m_deniedFilesList = null;
		}
	}

	public void setAllowedFilesList(String s)
	{
		if(s != null)
		{
			m_allowedFilesList = "," + s.replaceAll(" ", "").toLowerCase() + ",";
		}
		else
		{
			m_allowedFilesList = null;
		}
	}

	public void setTotalMaxFileSize(long l)
	{
		totalMaxFileSize = l;
	}

	public void setMaxFileSize(long l)
	{
		maxFileSize = l;
	}

	private void getDataSection()
	{
		int i = m_currentIndex;
		int j = 0;
		int k = m_boundary.length();
		m_startData = m_currentIndex;
		m_endData = 0;
		while(i < m_totalBytes)
		{
			if(m_binArray[i] == (byte) m_boundary.charAt(j))
			{
				if(j == k - 1)
				{
					m_endData = ((i - k) + 1) - 3;
					break;
				}
				i++;
				j++;
			}
			else
			{
				i++;
				j = 0;
			}
		}
		m_currentIndex = m_endData + k + 3;
	}

	private String getDataHeader()
	{
		int i = m_currentIndex;
		int j = 0;
		for(boolean flag1 = false; !flag1;)
		{
			if(m_binArray[m_currentIndex] == 13 && m_binArray[m_currentIndex + 2] == 13)
			{
				flag1 = true;
				j = m_currentIndex - 1;
				m_currentIndex = m_currentIndex + 2;
			}
			else
			{
				m_currentIndex++;
			}
		}
		String s = "";
		try
		{
			s = new String(m_binArray, i, (j - i) + 1, "UTF-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return s;
	}

	private void checkFileExt(MyFile file) throws Exception
	{
		if(file.getFileExt().length() > 0)
		{
			String _ext = "," + file.getFileExt() + ",";
			if(m_deniedFilesList != null)
			{
				if(m_deniedFilesList.indexOf(_ext) != -1)
				{
					throw new Exception("MyRequestException:文件扩展名属于拒绝范围");
				}
			}
			if(m_allowedFilesList != null)
			{
				if(m_allowedFilesList.indexOf(_ext) == -1)
				{
					throw new Exception("MyRequestException:文件扩展名不在允许范围");
				}
			}
		}
		else
		{
			throw new Exception("MyRequestException:文件扩展名无效无法接收");
		}
	}

	private void checkFileSize(MyFile file, Long size) throws Exception
	{
		if(maxFileSize > 0L && (long) (size) > maxFileSize)
		{
			throw new Exception("MyRequestException:文件大小超出范围 : " + file.getFileName());
		}
	}

	private static MyFile getFileForHeader(String header)
	{
		MyFile file = new MyFile();
		file.setFieldName(getDataFieldValue(header, "name"));
		if(header.indexOf("filename") > 0)
		{
			file.setContentType(getContentType(header));
			file.setContentDisp(getContentDisp(header));
			String fileName = getFileName(getDataFieldValue(header, "filename"));
			String fileExt = getFileExt(fileName);
			if(fileName.length() > 0 && fileExt.length() > 0)
			{
				fileName = fileName.substring(0, fileName.length() - fileExt.length()) + fileExt;// 转化后缀名为小写
			}
			file.setFileName(fileName);
			file.setFileExt(fileExt);
		}
		return file;
	}

	private static String getDataFieldValue(String s, String s1)
	{
		String s2 = "";
		String s3 = "";
		int i = 0;
		s2 = s1 + "=" + '"';
		i = s.indexOf(s2);
		if(i > 0)
		{
			int j = i + s2.length();
			int k = j;
			s2 = "\"";
			int l = s.indexOf(s2, j);
			if(k > 0 && l > 0)
			{
				s3 = s.substring(k, l);
			}
		}
		return s3;
	}

	private static String getContentType(String s)
	{
		String s1 = "";
		String s2 = "";
		int i = 0;
		s1 = "Content-Type:";
		i = s.indexOf(s1) + s1.length();
		if(i != -1)
		{
			int j = s.length();
			s2 = s.substring(i, j);
		}
		return s2;
	}

	private static String getContentDisp(String s)
	{
		String s1 = "";
		int i = 0;
		int j = 0;
		i = s.indexOf(":") + 1;
		j = s.indexOf(";");
		s1 = s.substring(i, j);
		return s1;
	}

	private static String getFileExt(String s)
	{
		if(s == null)
		{
			return "";
		}
		int i = s.lastIndexOf(46);// 46="."
		return i != -1 ? s.substring(i + 1).toLowerCase() : "";
	}

	private static String getFileName(String s)
	{
		int i = 0;
		i = s.lastIndexOf(47);
		if(i != -1)
		{
			return s.substring(i + 1, s.length());
		}
		i = s.lastIndexOf(92);
		if(i != -1)
		{
			return s.substring(i + 1, s.length());
		}
		else
		{
			return s;
		}
	}
}
