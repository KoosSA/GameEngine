package engine.models;

public class RawModel {

	private int vaoId;
	private int count;

	public RawModel(int vaoId, int count) {
		this.vaoId = vaoId;
		this.count = count;
	}

	public int getVaoId() {
		return vaoId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


}
