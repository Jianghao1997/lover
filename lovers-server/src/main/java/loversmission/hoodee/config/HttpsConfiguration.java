// package loversmission.hoodee.config;


// import org.apache.catalina.Context;
// import org.apache.catalina.connector.Connector;
// import org.apache.tomcat.util.descriptor.web.SecurityCollection;
// import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
// import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// /**
//  * http 8080 端口转发，后端配置https可以开启此配置
//  * @version 1.0
//  * @author: jianghao
//  * @createTime: 2022年07月29日 11:09
//  */
// @Configuration
// public class HttpsConfiguration {

//     @Value("${http-port}")
//     private int port;

//     @Value("${server.port}")
//     private int sslPort;

//     @Bean
//     public ServletWebServerFactory servletContainer() {
//         TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//             @Override
//             protected void postProcessContext(Context context) {
//                 SecurityConstraint securityConstraint = new SecurityConstraint();
//                 securityConstraint.setUserConstraint("CONFIDENTIAL");
//                 SecurityCollection collection = new SecurityCollection();
//                 collection.addPattern("/*");
//                 securityConstraint.addCollection(collection);
//                 context.addConstraint(securityConstraint);
//             }
//         };
//         tomcat.addAdditionalTomcatConnectors(redirectConnector());
//         return tomcat;
//     }

//     private Connector redirectConnector() {
//         Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
//         connector.setScheme("http");
//         connector.setPort(port);
//         connector.setSecure(false);
//         connector.setRedirectPort(sslPort);
//         return connector;
//     }
// }
