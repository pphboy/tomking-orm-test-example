package test.tomking.factory;

import org.junit.Test;
import org.tomking.Tomking.factory.MapperFactory;

import com.pipihao.tomking.mapper.ClazzMapper;
import com.pipihao.tomking.pojo.Clazz;

/**
 * @author pipihao
 * @email pphboy@qq.com
 * @date 2021/3/3 16:58
 */
public class TestFactory {

    @Test
    public void testFactory() throws InstantiationException, IllegalAccessException {
        TomkingSessionFactory.getSession(Clazz.class);
        System.out.println("TestFactory");
    }
    
    /**
     * 测试工具类的创建
     */
    @Test
    public void testMapperFactory() {
    	MapperFactory mf = new MapperFactory();
    	ClazzMapper clazzMapper = mf.getMapperInstance(ClazzMapper.class);
    }


}
