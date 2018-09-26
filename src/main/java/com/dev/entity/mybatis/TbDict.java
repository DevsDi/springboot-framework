package com.dev.entity.mybatis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**

 * @ClassName: TbDict

 * @Description: 字典表数据

 * @author: wen.dai

 * @date: 2018年5月22日 下午6:40:01


 */
@ToString
public class TbDict {
	
    @Getter
    @Setter
	private String id;
    
    @Getter
    @Setter
    private String type_name;
    
    @Getter
    @Setter
	private String ecode;
    
    @Getter
    @Setter
	private String ename;
	
}

