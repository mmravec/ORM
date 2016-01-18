package orm.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import orm.SimpleORM;

public class Test {
	static final String URL = "jdbc:mysql://localhost/test";
	static final String USER = "root";
	static final String PASSWORDD = "";

	public static void main(String[] args) throws Exception {
		try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORDD)) {
			
			SimpleORM orm = new SimpleORM(connection);

//			System.out.println(orm.createCreate(Student.class));
//			System.out.println(orm.createInsert(Student.class));
//			System.out.println(orm.createCreate(UserScore.class));
//			System.out.println(orm.createSelect(UserScore.class));
			
//		    Student student2 = new Student(1, "Erik", "Hajnik", 35);
//			orm.insert(student);
//			orm.delete(Student.class, 3);
			Student student = orm.find(Student.class,1);
			student.setMeno("Erika");
			student.setPriezvisko("Hajnik");
			orm.update(student);
			
//			List<UserScore> scores = orm.select(UserScore.class);
//			System.out.println(scores);

			System.out.println(orm.select(Student.class));
		}
	}
}
