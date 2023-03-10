package test.tomking.mapper;

import org.junit.Test;
import org.tomking.Tomking.factory.MapperFactory;

import com.pipihao.tomking.mapper.ClazzMapper;
import com.pipihao.tomking.pojo.Clazz;

/**
 * 
 * @author 皮豪
 * @date 2023年3月10日
 */
public class TestClazzMapper {

    /**
     * 测试开发 对象绑定
     * @date 2023年3月8日 23:38:28
     */
    @Test
    public void  testSelected() {
    	MapperFactory mf = new MapperFactory();
    	ClazzMapper cm = mf.getMapperInstance(ClazzMapper.class);
    	// 条件
    	Clazz clz = new Clazz();
    	clz.setId(1);

    	Integer a=  1;
    	// 测试
    	long start = System.currentTimeMillis();
    	cm.getByCondiction(clz);
    	System.out.println("运行时间"+(System.currentTimeMillis()-start));

    	start = System.currentTimeMillis();
    	clz.setId(10);
    	cm.getByCondiction(clz);
    	System.out.println("运行时间"+(System.currentTimeMillis()-start));
    }
    
    @Test
    public void testInsert() {
    	MapperFactory mf = new MapperFactory();
    	ClazzMapper cm = mf.getMapperInstance(ClazzMapper.class);
    	// 条件
    	Clazz clz = new Clazz();
    	clz.setId(20);
    	clz.setCname("软件2002");
    	
    	System.out.println(cm.saveOne(clz));;
    }
    
    @Test
    public void testUpdate() {
    	MapperFactory mf = new MapperFactory();
    	ClazzMapper cm = mf.getMapperInstance(ClazzMapper.class);

    	Clazz clz = new Clazz();
    	clz.setId(19);
    	clz.setCname("软件2222");
    	
    	// update 只会返回 -1，因为同时可以修改很多个
    	System.out.println(cm.updateById(clz));;
    }

    @Test
    public void testDelete() {
    	MapperFactory mf = new MapperFactory();
    	ClazzMapper cm = mf.getMapperInstance(ClazzMapper.class);

    	Clazz clz = new Clazz();
    	clz.setId(19);
    	System.out.println(cm.deleteById(clz));
    	
    }
    

}
