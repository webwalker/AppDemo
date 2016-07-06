混淆时注意事项：
    添加Android默认混淆配置${sdk.dir}/tools/proguard/proguard-android.txt
    不要混淆xUtils中的注解类型，添加混淆配置：-keep class * extends java.lang.annotation.Annotation { *; }
    对使用DbUtils模块持久化的实体类不要混淆，或者注解所有表和列名称@Table(name="xxx")，@Id(column="xxx")，@Column(column="xxx"),@Foreign(column="xxx",foreign="xxx")；
