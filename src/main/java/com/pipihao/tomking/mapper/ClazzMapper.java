package com.pipihao.tomking.mapper;

import com.pipihao.tomking.pojo.Clazz;

import org.tomking.Tomking.Annotation.exec.Modify;
import org.tomking.Tomking.Annotation.exec.Select;
import org.tomking.Tomking.Annotation.mapper.Param;

import java.util.List;

/**
 * @author pipihao
 * @email pphboy@qq.com
 * @date 2021/3/3 17:34
 */
public interface ClazzMapper {

    @Select("select * from clazz where id = #{id} and cname = #{cname}")
    List<Clazz> getClazz(@Param("id") Integer id, @Param("cname") String cname);
    
    @Select("select * from clazz where id = #{id}")
    List<Clazz> getOne(@Param("id") Integer id);
    
    @Select("select * from clazz where id = #{clz.id} ")
    List<Clazz> getByCondiction(@Param("clz")Clazz on);

    @Modify("insert into clazz values(#{clazz.id},#{clazz.cname})")
    int saveOne(@Param("clazz")Clazz on);
    
    @Modify("update clazz set cname = #{clazz.cname} where id = #{clazz.id} ")
    int updateById(@Param("clazz")Clazz on);
    
    @Modify("delete from clazz where id = #{clz.id}")
    int deleteById(@Param("clz") Clazz clz) ;
    
}
