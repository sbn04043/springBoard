package com.nc13.springBoard.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//MyBatis는 데이터베이스 통신을 위한 객체를
//자기 자신이 내부적으로 컨트롤 한다.
//따라서 통신을 위한 설정 값을 application.properties에 저장하고
//실행시킬 쿼리들을 xml 파일로 분리한다.
//그 후 MyBatis에서 쿼리를 실행시킬 객체들을 스프링이 관리할 수 있도록
//클래스를 만들어 관리한다.

@Configuration
public class myBatisConfig {
    //컨트롤러, 서비스, config 같이 스프링 프레임워크가 관리하는
    //객체들 중 특수한 성격을 띄는 객체들은 어노테이션을 붙이지만
    //특수한 성격을 띄지 않거나 별개의 라이브러리 객체일 경우,
    //@Bean 이라는 어노테이션을 붙인다.

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mappers/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);

    }
}