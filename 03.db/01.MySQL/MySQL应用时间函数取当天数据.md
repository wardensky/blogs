# MySQL应用时间函数取当天数据


```

set @var1 = (select date_format(now(),"%Y-%m-%d"));
select * from monitor_task where end_time > @var1 order by start_time desc;
select * from monitor_task_item where end_time > @var1  order by start_time desc;
select * from monitor_task_error where create_time > @var1  order by create_time desc;

```
