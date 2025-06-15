package com.example.batchdemo.batchdemo.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class StudentRowMapper implements RowMapper<Student>{

    @Override
    public Student mapRow(ResultSet rs, int arg1) throws SQLException {
      Student student = new Student();
      student.setId(rs.getInt("id"));
      student.setFirstName(rs.getString("firstName"));
      student.setLastName(rs.getString("lastName"));
      student.setEmail(rs.getString("email"));
      return student;
    }

}
