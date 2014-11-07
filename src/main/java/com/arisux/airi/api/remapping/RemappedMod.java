package com.arisux.airi.api.remapping;

public class RemappedMod
{
	public String oldID, newID, classLocation;

	public RemappedMod(String oldID, String newID, String classLocation)
	{
		this.oldID = oldID;
		this.newID = newID;
		this.classLocation = classLocation;
	}

	public String getClassLocation()
	{
		return classLocation;
	}

	public String getNewID()
	{
		return newID;
	}

	public String getOldID()
	{
		return oldID;
	}
}
