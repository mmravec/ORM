package orm;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SimpleORM {
	private final Connection connection;

	public SimpleORM(Connection connection) {
		this.connection = connection;
	}

	public String createCreate(Class<?> clazz) throws Exception {
		StringBuffer sb = new StringBuffer();
		String name = clazz.getSimpleName();
		Field[] fields = clazz.getDeclaredFields();

		sb.append("CREATE TABLE ");
		sb.append(name);
		sb.append(" (\n");

		boolean first = true;
		for (Field field : fields) {
			if (!first)
				sb.append(",\n");
			sb.append("  " + field.getName() + " " + getDBType(field.getType()));
			first = false;
		}

		sb.append("\n)");

		return sb.toString();
	}

	public String createSelect(Class<?> clazz) throws Exception {
		StringBuffer sb = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();

		sb.append("SELECT ");

		boolean first = true;
		for (Field field : fields) {
			if (!first)
				sb.append(", ");
			sb.append(field.getName());
			first = false;
		}

		sb.append(" FROM ");

		String name = clazz.getSimpleName();
		sb.append(name);

		return sb.toString();
	}

	public String createInsert(Class<?> clazz) throws Exception {
		StringBuffer sb = new StringBuffer();
		String name = clazz.getSimpleName();
		Field[] fields = clazz.getDeclaredFields();

		sb.append("INSERT INTO ");
		sb.append(name);
		sb.append(" (");

		boolean first = true;
		for (Field field : fields) {
			if (!first)
				sb.append(", ");
			sb.append(field.getName());
			first = false;
		}

		sb.append(") VALUES (");

		first = true;
		for (Field field : fields) {
			if (!first)
				sb.append(", ");
			sb.append("?");
			first = false;
		}

		sb.append(")");

		return sb.toString();
	}

	private String getDBType(Class<?> javaType) {
		if (javaType.equals(String.class)) {
			return "VARCHAR(128)";
		} else if (javaType.equals(Long.TYPE)) {
			return "integer";
		} else if (javaType.equals(Integer.TYPE)) {
			return "integer";
		}
		throw new RuntimeException("Unsup. java type " + javaType);
	}

	public <T> List<T> select(Class<T> clazz) throws Exception {
		String query = createSelect(clazz);
		List<T> items = new ArrayList<>();
		Field[] fields = clazz.getDeclaredFields();

		try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(query)) {
			while (rs.next()) {
				T item = clazz.newInstance();

				int column = 1;
				for (Field field : fields) {
					Object value = rs.getObject(column);
					field.setAccessible(true);
					field.set(item, value);
					column++;
				}
				items.add(item);
			}
		}

		return items;
	}

	public <T> void insert(T item) throws Exception {
		Class<T> clazz = (Class<T>) item.getClass();
		String update = createInsert(clazz);
		Field[] fields = clazz.getDeclaredFields();

		try (PreparedStatement ps = connection.prepareStatement(update)) {
			int column = 1;
			for (Field field : fields) {
				field.setAccessible(true);
				Object value = field.get(item);
				ps.setObject(column, value);
				column++;
			}

			ps.execute();
		}
	}

	public String delete(Class<?> clazz, int id) throws Exception {
		StringBuffer sb = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
		String idecko = "";

		sb.append("DELETE ");

		boolean first = true;
		for (Field field : fields) {
			if (!first) {
				// sb.append(", ");

			}
			if (first)
				idecko = field.getName();
			// sb.append(field.getName());
			first = false;
		}

		sb.append(" FROM ");

		String name = clazz.getSimpleName();
		sb.append(name);

		sb.append(" WHERE ");
		sb.append(idecko);
		sb.append("=" + id + ";");

		Statement statement = connection.createStatement();
		statement.executeUpdate(sb.toString());

		return sb.toString();
	}

	public void update(Object object) throws Exception {
		Class<?> clazz = object.getClass();
		StringBuffer sb = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
		long id = 0;
		String idecko = "";

		sb.append("UPDATE " + clazz.getSimpleName() + " SET ");

		boolean first = true;
		for (Field field : fields) {
			field.setAccessible(true);
			if (!first) {
				sb.append(", ");

			}
			if (first) {
				idecko = field.getName();
				// field == id; object.getID()
				id = (long) field.get(object);
				System.out.println(id);

			}

			sb.append(field.getName() + "=");
			if (field.get(object) instanceof String) {
				sb.append("'");
				sb.append(field.get(object));
				sb.append("'");
			} else
				sb.append(field.get(object));
			first = false;
		}

		sb.append(" WHERE ");
		sb.append(idecko);
		sb.append("=" + id + ";");

		System.out.println(sb.toString());
		Statement statement = connection.createStatement();
		statement.executeUpdate(sb.toString());

	}

	public <T> T find(Class<T> clazz, int id) throws Exception {
		StringBuffer sb = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
		String idecko = "";

		sb.append("SELECT ");

		boolean first = true;
		for (Field field : fields) {
			if (!first) {
				sb.append(", ");

			}
			if (first)
				idecko = field.getName();
			sb.append(field.getName());
			first = false;
		}

		sb.append(" FROM ");

		String name = clazz.getSimpleName();
		sb.append(name);

		sb.append(" WHERE ");
		sb.append(idecko);
		sb.append("=" + id + ";");

		T item = clazz.newInstance();
		try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sb.toString())) {
			while (rs.next()) {

				int column = 1;
				for (Field field : fields) {
					Object value = rs.getObject(column);
					field.setAccessible(true);
					field.set(item, value);
					column++;
				}
				// items.add(item);
			}
		}
		return item;
	}

}