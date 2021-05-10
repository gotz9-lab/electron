package com.github.gotz9.electron.service;

import com.github.gotz9.electron.ServiceSpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringContextTest {

    @Test
    public void contextTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ServiceSpringConfiguration.class);
        context.refresh();

        AuthenticateService bean;
        try {
            bean = context.getBean(AuthenticateService.class);
            System.out.println(bean);
        } catch (NoSuchBeanDefinitionException e) {
            System.out.println(e.getLocalizedMessage());
            Assert.fail();
        }
    }

}
