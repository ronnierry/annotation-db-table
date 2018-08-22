package com.ronnie.annotationdbtable.entity;

import com.ronnie.annotationdbtable.annotation.Column;
import com.ronnie.annotationdbtable.annotation.Id;
import com.ronnie.annotationdbtable.annotation.Table;

/**
 * @Description:
 * @Author: rongyu
 * @CreateDate: 2018/8/22$ 16:20$
 * @Remark:
 */
@Table(name = "member")
public class Member {
    @Id
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "age",  columnDefinition = "int(11)")
    private Long age;
}
