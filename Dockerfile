FROM tomcat:9.0.60-jre11
COPY target/*.war /usr/local/tomcat/webapps
EXPOSE 8080
CMD chmod +x /usr/local/tomcat/bin/catalina.sh
CMD ["catalina.sh", "run"] 
