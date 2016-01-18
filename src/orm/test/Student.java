package orm.test;

public class Student {
	private long id;
	private String meno;
	private String priezvisko;
	private int vek;

	public Student() {
	}
	
	public Student(int id, String meno, String priezvisko, int vek) {
		super();
		this.meno = meno;
		this.priezvisko = priezvisko;
		this.vek = vek;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMeno() {
		return meno;
	}
	public void setMeno(String meno) {
		this.meno = meno;
	}
	public String getPriezvisko() {
		return priezvisko;
	}
	public void setPriezvisko(String priezvisko) {
		this.priezvisko = priezvisko;
	}
	public int getVek() {
		return vek;
	}
	public void setVek(int vek) {
		this.vek = vek;
	}
	@Override
	public String toString() {
		return "Student [meno=" + meno + ", priezvisko=" + priezvisko + ", vek=" + vek + "]";
	}
	
	
}
