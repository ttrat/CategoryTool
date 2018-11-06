package model;

public class Provider {
	
	public Provider(long id, String name, String recordFile) {
		this.id = id;
		this.name = name;
		this.recordFile = recordFile;
	}
	
	public Provider(String name, String recordFile) {
		this.name = name;
		this.recordFile = recordFile;
	}
	
	private long id;
	private String name;
	private String recordFile;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRecordFile() {
		return recordFile;
	}
	public void setRecordFile(String recordFile) {
		this.recordFile = recordFile;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
