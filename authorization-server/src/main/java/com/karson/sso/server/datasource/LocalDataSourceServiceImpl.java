/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karson.sso.server.datasource;

import com.karson.sso.sys.env.EnvUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * local data source.
 *
 * @author Nacos
 */
public class LocalDataSourceServiceImpl implements DataSourceService {
    Logger logger = LoggerFactory.getLogger(LocalDataSourceServiceImpl.class);

    private final String jdbcDriverName = "org.apache.derby.jdbc.EmbeddedDriver";

    private final String userName = "auth";

    private final String password = "auth";

    private final String derbyBaseDir = "data" + File.separator + "derby-data";

    private final String derbyShutdownErrMsg = "Derby system shutdown.";

    private volatile JdbcTemplate jt;

    private volatile TransactionTemplate tjt;

    private boolean initialize = false;

    private boolean jdbcTemplateInit = false;

    private String healthStatus = "UP";

    @PostConstruct
    @Override
    public synchronized void init() throws Exception {
        if (!initialize) {
            final String jdbcUrl =
                    "jdbc:derby:" + Paths.get(Paths.get(System.getProperty("user.home"), "authserver").toString(), derbyBaseDir).toString()
                            + ";create=true";
            initialize(jdbcUrl);
            initialize = true;
        }
    }

    @Override
    public void reload() throws IOException {

    }

//    @Override
//    public synchronized void reload() {
//        DataSource ds = jt.getDataSource();
//        if (ds == null) {
//            throw new RuntimeException("datasource is null");
//        }
//        try {
//            execute(ds.getConnection(), "META-INF/schema.sql");
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            throw new RuntimeException("load schema.sql error.", e);
//        }
//    }

    public DataSource getDatasource() {
        return jt.getDataSource();
    }

    /**
     * Clean and reopen Derby.
     *
     * @throws Exception exception.
     */
//    public void cleanAndReopenDerby() throws Exception {
//        doDerbyClean();
//        final String jdbcUrl =
//                "jdbc:derby:" + Paths.get(Paths.get(Paths.get(System.getProperty("user.home"), "authserver").toString(), derbyBaseDir).toString() + ";create=true";
//        initialize(jdbcUrl);
//    }

//    public void restoreDerby(String jdbcUrl, Callable<Void> callable) throws Exception {
//        doDerbyClean();
//        callable.call();
//        initialize(jdbcUrl);
//    }

//    private void doDerbyClean() throws Exception {
//        logger.warn("use local db service for reopenDerby");
//        try {
//            DriverManager.getConnection("jdbc:derby:;shutdown=true");
//        } catch (Exception e) {
//            // An error is thrown when the Derby shutdown is executed, which should be ignored
//            if (!StringUtils.containsIgnoreCase(e.getMessage(), derbyShutdownErrMsg)) {
//                throw e;
//            }
//        }
//        DiskUtils.deleteDirectory(Paths.get(EnvUtil.getNacosHome(), derbyBaseDir).toString());
//    }

    private synchronized void initialize(String jdbcUrl) {
        DataSourcePoolProperties poolProperties = DataSourcePoolProperties.build(EnvUtil.getEnvironment());
        poolProperties.setDriverClassName(jdbcDriverName);
        poolProperties.setJdbcUrl(jdbcUrl);
        poolProperties.setUsername(userName);
        poolProperties.setPassword(password);
        HikariDataSource ds = poolProperties.getDataSource();
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(ds);
        if (jdbcTemplateInit) {
            jt.setDataSource(ds);
            tjt.setTransactionManager(tm);
        } else {
            jt = new JdbcTemplate();
            jt.setMaxRows(50000);
            jt.setQueryTimeout(5000);
            jt.setDataSource(ds);
            tjt = new TransactionTemplate(tm);
            tjt.setTimeout(5000);
            jdbcTemplateInit = true;
        }
     //   reload();
    }

    @Override
    public boolean checkMasterWritable() {
        return true;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jt;
    }

    @Override
    public TransactionTemplate getTransactionTemplate() {
        return tjt;
    }

    @Override
    public String getCurrentDbUrl() {
        return "jdbc:derby:" + Paths.get(Paths.get(System.getProperty("user.home"), "authserver").toString()+ File.separator + derbyBaseDir + ";create=true");
    }

    @Override
    public String getHealth() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    /**
     * Load sql.
     *
     * @param sqlFile sql.
     * @return sqls.
     * @throws Exception Exception.
     */
//    private List<String> loadSql(String sqlFile) throws Exception {
//        List<String> sqlList = new ArrayList<String>();
//        InputStream sqlFileIn = null;
//        try {
//            File file = new File(
//                    Paths.get(Paths.get(System.getProperty("user.home"), "authserver").toString() + File.separator + "conf" + File.separator + "schema.sql");
//            if (StringUtils.isBlank( Paths.get(System.getProperty("user.home"), "authserver").toString()) || !file.exists()) {
//                ClassLoader classLoader = getClass().getClassLoader();
//                URL url = classLoader.getResource(sqlFile);
//                sqlFileIn = url.openStream();
//            } else {
//                sqlFileIn = new FileInputStream(file);
//            }
//
//            StringBuilder sqlSb = new StringBuilder();
//            byte[] buff = new byte[1024];
//            int byteRead = 0;
//            while ((byteRead = sqlFileIn.read(buff)) != -1) {
//                sqlSb.append(new String(buff, 0, byteRead,"UTF-8"));
//            }
//
//            String[] sqlArr = sqlSb.toString().split(";");
//            for (int i = 0; i < sqlArr.length; i++) {
//                String sql = sqlArr[i].replaceAll("--.*", "").trim();
//                if (StringUtils.isNotEmpty(sql)) {
//                    sqlList.add(sql);
//                }
//            }
//            return sqlList;
//        } catch (Exception ex) {
//            throw new Exception(ex.getMessage());
//        } finally {
//            IoUtils.closeQuietly(sqlFileIn);
//        }
//    }

    /**
     * Execute sql.
     *
     * @param conn    connect.
     * @param sqlFile sql.
     * @throws Exception Exception.
     */
//    private void execute(Connection conn, String sqlFile) throws Exception {
//        try (Statement stmt = conn.createStatement()) {
//            List<String> sqlList = loadSql(sqlFile);
//            for (String sql : sqlList) {
//                try {
//                    stmt.execute(sql);
//                } catch (Exception e) {
//                    logger.warn(e.getMessage());
//                }
//            }
//        }
//    }

}
