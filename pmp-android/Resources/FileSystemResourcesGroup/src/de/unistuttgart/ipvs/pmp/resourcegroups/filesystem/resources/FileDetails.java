package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import java.io.File;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Stores details of a file.
 * This is used for security reasons. If we would directly transfer a
 * File object, the user could easily modify files without using our resource
 * (e.g <code>File.delete()</code>) 
 * 
 * @author Patrick Strobel
 * @version 0.1.0
 */
public class FileDetails implements Parcelable {

	private String absolutePath;
	private String name;
	private String path;

	private boolean canRead;
	private boolean canWrite;

	private boolean isAbsolute;
	private boolean isDirectory;
	private boolean isFile;

	public static final Parcelable.Creator<FileDetails> CREATOR = new Parcelable.Creator<FileDetails>() {
		public FileDetails createFromParcel(Parcel source) {
			return createFromParcel(source);
		}

		public FileDetails[] newArray(int size) {
			return new FileDetails[size];
		}
	};
	
	private FileDetails() {}

	/**
	 * Creates a FileDetails object using data from a file object
	 * @param file Source of data used for this FileDetails object
	 */
	public FileDetails(File file) {
		absolutePath = file.getAbsolutePath();
		name = file.getName();
		path = file.getPath();

		canRead = file.canRead();
		canWrite = file.canWrite();

		isAbsolute = file.isAbsolute();
		isDirectory = file.isDirectory();
		isFile = file.isFile();
	}

	/**
	 * Returns true, if file is readable
	 * 
	 * @see File#canRead()
	 * @return True, if readable
	 */
	public boolean canRead() {
		return canRead;
	}

	/**
	 * Returns true, if file is writable
	 * 
	 * @see File#canWrite()
	 * @return True, if writable
	 */
	public boolean canWrite() {
		return canWrite;
	}

	/**
	 * Returns the absolute path of this file
	 * 
	 * @see File#getAbsolutePath()
	 * @return Absolute path
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}

	/**
	 * Returns the name of this file
	 * 
	 * @see File#getName()
	 * @return Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the path of this file
	 * 
	 * @see File#getPath()
	 * @return Path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns true, if path is absolute
	 * 
	 * @see File#isAbsolute()
	 * @return True, if absolute
	 */
	public boolean isAbsolute() {
		return isAbsolute;
	}

	/**
	 * Returns true, if this is a directory
	 * 
	 * @see File#isDirectory()
	 * @return True, if directory
	 */
	public boolean isDirectory() {
		return isDirectory;
	}

	/**
	 * Returns true, if this is a file
	 * 
	 * @see File#isFile()
	 * @return True, if file
	 */
	public boolean isFile() {
		return isFile;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// Store booleans in parcel
		boolean[] bools = new boolean[5];
		bools[0] = canRead;
		bools[1] = canWrite;
		bools[2] = isAbsolute;
		bools[3] = isDirectory;
		bools[4] = isFile;		
		dest.writeBooleanArray(bools);
		
		// Store strings in parcel
		dest.writeString(absolutePath);
		dest.writeString(name);
		dest.writeString(path);
	}
	
	/**
	 * Recreates the FileDetails object from a parcel
	 * @param source Parcel
	 * @return Recovered FileDetails object
	 */
	public static FileDetails createFromParcel(Parcel source) {
		// TODO security check
		FileDetails f = new FileDetails();
		
		// Restore booleans from parcel
		boolean[] bools = new boolean[5];
		source.readBooleanArray(bools);
		f.canRead 		= bools[0];
		f.canWrite		= bools[1];
		f.isAbsolute	= bools[2];
		f.isDirectory	= bools[3];
		f.isFile		= bools[4];	
		
		// Restore strings from parcel
		f.absolutePath 	= source.readString();
		f.name			= source.readString();
		f.path			= source.readString();
		return f;
	}
}
