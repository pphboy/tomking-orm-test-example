package test.tomking.pattern;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.collections.set.SynchronizedSortedSet;
import org.junit.Test;
import org.tomking.Tomking.Utils.ResultSetUtils;
import org.tomking.Tomking.config.TomkingConfig;
import org.tomking.Tomking.exception.MapperParamBindingException;

import com.pipihao.tomking.pojo.Clazz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pipihao
 * @email pphboy@qq.com
 * @date 2021/3/2 20:54
 */
public class LearnPattern {

    /**
     * 这里通过正则记录，SQL 中的 替代字符的位置，并记录位置
     */
    @Test
    public void learnMatches(){
        String sql = "select * from class where id = #{id} and cname = #{cname} ";
        String pattern = ".*#\\{.*\\}.*";
        System.out.println(Pattern.matches(pattern,sql));
    }

    @Test
    public void leanPattern() throws SQLException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        long start = System.currentTimeMillis();
        String sql = "select * from class where id = #{id} and cname = #{cname} ";
        String pattern = "#\\{(.*?)\\}";
        //生成正则对象
        Pattern r = Pattern.compile(pattern);
        /*匹配*/
        Matcher m = r.matcher(sql);
        /*存field的列表*/
        List<String> fieldList = new ArrayList<>();
        if(m.find()){
            /*记录每一个匹配对象的位置
            * fieldList的索引就是其位置
            * */
            do{
                String field = m.group().replaceAll("\\W","");
                System.out.println("field: "+field);
                fieldList.add(field);
            }while (m.find());
            System.out.println(fieldList);
        }

        /*将占位符换成问号*/
        String rSql = sql.replaceAll(pattern,"?");
        System.out.println("rSql: "+rSql);
        /*注入值的对象*/
        Clazz clazz = new Clazz(2,"计应2班");
        BeanMap beanMap = new BeanMap(clazz);
        /*连接器*/
        DruidPooledConnection connection = TomkingConfig.instance().getDatabase().getConnection();
        /*匹配sqwl*/
        PreparedStatement preparedStatement = connection.prepareStatement(rSql);
        for(int i = 0; i < fieldList.size(); i++){
        	// 原来 是 先保存  占位符的 位置 ，然后把值存到map里，然后再把值 按占位符的顺序存到 list内
        	// 不得不说太妙了
        	// 所以需要用一个Map来存 Bean的实例，然后通过名称就能取到其值
        	// 就是在取的时候需要判断其是否带 '.' 
            preparedStatement.setString(i+1,beanMap.get(fieldList.get(i)).toString());
        }
        System.out.println(preparedStatement.toString());;
        //执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        /*将数据转成对象列表*/
        List list = ResultSetUtils.populate(resultSet, Clazz.class);
        System.out.println("第一次查询花了: "+(System.currentTimeMillis()-start)+"毫秒");
        System.out.println(list);
    }


    @Test
    public void leanPatternObjBinding() throws SQLException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        long start = System.currentTimeMillis();
        String sql = "select * from class where id = #{clz.id} and cname = #{clz.cname} or 1=#{other} ";
        String pattern = "#\\{(.*?)\\}";
        //生成正则对象
        Pattern r = Pattern.compile(pattern);
        /*匹配*/
        Matcher m = r.matcher(sql);
        Clazz clz = new Clazz(1,"软件工程2002");
        Map<String,Object> valueMap = new HashMap<>();
        valueMap.put("clz", clz);
        valueMap.put("other", "中国华为");

        List<String> fieldList=  new ArrayList<>();

        while(m.find()) {
        	System.out.println(m.group(1));
        	fieldList.add(m.group(1));
        }
		String solveSql = sql.replaceAll(pattern, "?");
//        System.out.println(solveSql);
		
        /*注入值的对象*/
        /*连接器*/
        DruidPooledConnection connection = TomkingConfig.instance().getDatabase().getConnection();
        /*匹配sqwl*/
        PreparedStatement preparedStatement = connection.prepareStatement(solveSql);

		BeanMap objectMap;
		String key;
		Object valueObject ;
        for(int i = 0; i < fieldList.size(); i++){
        	// 在这里将obj设置成mapBean
        	String paramStr = fieldList.get(i);
//        	System.out.println("Exec Select value: "+fieldList.get(i));

        	String[] objectParamDecompose = paramStr.split("\\.");
//        	System.out.println("length = "+objectParamDecompose.length+" "+Arrays.toString(objectParamDecompose));

        	// 如果数据长度为0 ，则说明只有一个参数
        	if(objectParamDecompose.length == 1) {
				// 这需要做类型判断
        		// 可以做一个方法，传入什么类型的参数做什么类型的判断
				preparedStatement.setString(i+1,valueMap.get(objectParamDecompose[0]).toString());
        	}else if(objectParamDecompose.length == 2) {
        		key = objectParamDecompose[0];

        		// 拿到key对应的值
        		valueObject = valueMap.get(key);
        		if(valueObject == null) {
        			// TODO: 这里还是需要加强一下，把方法和类都补上
        			throw new MapperParamBindingException(paramStr+"不存在");
        		}
        		// 如果没有转成BeanMap先转成BeanMap
        		if(!( valueObject instanceof BeanMap)) {
        			objectMap  = new BeanMap(valueObject);
        			valueMap.put(key, objectMap);
        		}else {
        			// 因为之前已经转，这里直接强转
        			objectMap = (BeanMap)valueMap.get(key);
        		}
        		preparedStatement.setString(i+1,objectMap.get(objectParamDecompose[1]).toString());
        	}

        }
        System.out.println(preparedStatement.toString());
		
        System.out.println(System.currentTimeMillis()-start);
        
    }

}
