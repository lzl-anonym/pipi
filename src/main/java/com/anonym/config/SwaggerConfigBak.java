package com.anonym.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger 配置
 */
//@Configuration
//@Profile({"dev", "prod"})
public class SwaggerConfigBak {

    /**
     * 分组名称
     */
    private String apiGroupName = "Anonym";

    /**
     * 文档标题
     */
    private String title = "通用后管系统 API";

    /**
     * 文档描述
     */
    private String description = "Common rear tube system api";

    /**
     * api版本
     */
    private String version = "1.0.0";

    /**
     * service url
     */
    private String serviceUrl = "http://172.16.0.145";

    /**
     * controller 包路径
     */
    @Value("${module-package-path}")
    private String packAge;

    /**
     * 默认 管理员 sa token 有效期1年 2020-07-22
     */
    private final String defaultToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkNDUyZmRkNC0xMTdmLTQ4MmYtOTAzNS04ZWQ1YzNmM2Q1NDkiLCJpZCI6MSwibmJmIjoxNTY1MTY5MTgwLCJleHAiOjE1NzM4MDkxODB9" +
            ".w8xGLkfHhn-qMvDgmb212u2m_X71y9DRE3eiB9U_UvEz2S-FXZKR1zaS95GwUv53JgEeZ8u2ABPhL_EH3fcCKw";

    /**
     * ServiceApi 可以定义多个API组
     */
    @Bean
    public Docket backApi() {
        System.out.println("===============Loading SwaggerConfigBak====================");

        // 请求类型过滤规则
        Predicate<RequestHandler> controllerPredicate = getBackPredicate();
        // controller 包路径
        Predicate<RequestHandler> controllerPackage = RequestHandlerSelectors.basePackage(packAge);
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("x-access-token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).defaultValue(defaultToken).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2).groupName("BACK").forCodeGeneration(true).select().apis(controllerPackage).apis(controllerPredicate).paths(PathSelectors.any()).build().apiInfo(serviceApiInfo()).globalOperationParameters(pars);
    }

    /**
     * ServiceApi 描述信息
     *
     * @return ApiInfo
     */
    private ApiInfo serviceApiInfo() {
        // 大标题
        return new ApiInfoBuilder().title(title)
                // 详细描述
                .description(description)
                // 版本
                .version(version).termsOfServiceUrl(serviceUrl).build();
    }

    /**
     * 定义swagger 对请求类型的过滤规则 只显示ajax请求的Controller
     *
     * @return Predicate<RequestHandler>
     */
    private Predicate<RequestHandler> getBackPredicate() {
        return Predicates.and(RequestHandlerSelectors.withClassAnnotation(RestController.class));
    }
}
