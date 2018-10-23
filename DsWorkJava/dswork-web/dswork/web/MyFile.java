package dswork.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Request表单文件
 * @author skey
 */
public class MyFile
{
	private String fieldName = "";
	private String fileName = "";
	private String fileExt = "";
	private String contentType = "";
	private String contentDisp = "";
	private byte[] fileData = null;

	/**
	 * 获取文件的字段名
	 * @return String
	 */
	public String getFieldName()
	{
		return fieldName;
	}

	/**
	 * 设置文件的字段名
	 * @param fieldName 字段名
	 */
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}

	/**
	 * 获取文件的文件名
	 * @return String
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * 设置文件的文件名
	 * @param fileName 文件名
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * 获取文件的扩展名
	 * @return String
	 */
	public String getFileExt()
	{
		return fileExt;
	}

	/**
	 * 设置文件的扩展名
	 * @param fileExt 扩展名
	 */
	public void setFileExt(String fileExt)
	{
		this.fileExt = fileExt;
	}

	/**
	 * 获取文件的Content-Type
	 * @return String
	 */
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * 设置文件的Content-Type
	 * @param contentType Content-Type
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	/**
	 * 获取文件的Content-Disposition
	 * @return String
	 */
	public String getContentDisp()
	{
		return contentDisp;
	}

	/**
	 * 设置文件的Content-Disposition
	 * @param contentDisp Content-Disposition
	 */
	public void setContentDisp(String contentDisp)
	{
		this.contentDisp = contentDisp;
	}

	/**
	 * 获取文件的字节流
	 * @return byte[]
	 */
	public byte[] getFileData()
	{
		return fileData;
	}

	/**
	 * 设置文件的字节流
	 * @param fileData 文件字节流
	 */
	public void setFileData(byte[] fileData)
	{
		this.fileData = fileData;
	}

	/**
	 * 根据指定文件名保存文件（如果存在则覆盖）
	 * @param filePath 指定文件名（全路径）
	 * @return boolean 成功返回true，失败返回false
	 */
	public boolean saveAs(String filePath)
	{
		return saveAs(filePath, true);
	}

	/**
	 * 根据指定文件名保存文件
	 * @param filePath 指定文件名（全路径）
	 * @param overwrite 是否覆盖，目标只能是文件
	 * @return boolean 成功返回true，失败返回false
	 */
	public boolean saveAs(String filePath, boolean overwrite)
	{
		try
		{
			File file = new File(filePath);
			if(!file.exists())
			{
				File parentFile = file.getParentFile();
				if(!parentFile.exists())
				{
					parentFile.mkdirs();// 创建所属目录
				}
			}
			else if(!overwrite || file.isDirectory())// 不可覆盖或是目录时
			{
				return false;
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(getFileData());
			outStream.close();
			return true;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}
}
