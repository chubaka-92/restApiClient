package ru.chubaka.springcourse.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.chubaka.springcourse.models.Person;

import java.nio.file.Path;
import java.util.*;

@Component
public class PersonRepositoryImpl implements PersonRepositoryCustom
{
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PersonRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Person> showFilter(String name, String email){
        StringBuilder where = new StringBuilder();
        SqlParameterSource namedParametrs = new MapSqlParameterSource().addValue("name",name)
                                                                       .addValue("email",email);
        if(name!=null || email!=null){
            if(name!=null && !name.isEmpty()){
                where.append("name=:name ");
            }
            if(email!=null && !email.isEmpty()){
                if (where.length()>0){
                    where.append("AND ");
                }
                where.append("email=:email ");
            }

            if (where.length()>0){ where.insert(0," WHERE ");}
        }
        return namedParameterJdbcTemplate.query("SELECT * FROM Person" + where.toString(),
                                                namedParametrs,
                                                new BeanPropertyRowMapper<>(Person.class));
    }
}
