/*
 *    Copyright 2009-2012 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.submitted.basetest;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BaseTest {

	private static SqlSessionFactory sqlSessionFactory;

	@BeforeClass
	public static void setUp() throws Exception {
		Connection conn = null;

		try {
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:mem:basetest", "sa", "");
			Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/basetest/CreateDB.sql");
			ScriptRunner runner = new ScriptRunner(conn);
			runner.setLogWriter(null);
			runner.setErrorLogWriter(null);
			runner.runScript(reader);
			conn.commit();
			reader.close();

			reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/basetest/mybatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			reader.close();

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Test
	public void shouldGetAUser() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			Mapper mapper = sqlSession.getMapper(Mapper.class);
			User user = mapper.getUser(1);
			Assert.assertEquals("User1", user.getName());
		} finally {
			sqlSession.close();
		}
	}

}