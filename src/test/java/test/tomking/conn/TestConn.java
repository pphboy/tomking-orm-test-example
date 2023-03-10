package test.tomking.conn;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.Test;
import org.tomking.Tomking.Utils.ResultSetUtils;
import org.tomking.Tomking.config.TomkingConfig;
import org.tomking.Tomking.factory.MapperFactory;

import com.pipihao.tomking.pojo.Clazz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author pipihao
 * @email pphboy@qq.com
 * @date 2021/3/2 19:46
 */
public class TestConn {

    /**
     * 这是查询，查询成功
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchFieldException
     */
    @Test
    public void testConn() throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        DruidPooledConnection connection = TomkingConfig.instance().getDatabase().getConnection();
        String sql= "select * from class";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List list = ResultSetUtils.populate(resultSet, Clazz.class);
        if(list.get(0) instanceof Clazz){
            System.out.println("是Clazz的实例");
        }
        System.out.println(list);
    }
    
    @Test
    public void debugDb() {
    	MapperFactory mapperFactory = new MapperFactory("");
    	DruidDataSource dds =  TomkingConfig.instance().getDatabase();
    	System.out.println(dds.getMaxActive());;
    }
    
    
    
}
