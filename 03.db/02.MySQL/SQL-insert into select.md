# SQL-insert into select


```

insert into stb_open_info_count select id, date_format(create_time,"%Y-%m-%d") from stb_open_info where status = 1;

```
