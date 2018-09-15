# 个left join的sql


```
create view charge_user as

select a.wechat_openid, b.nickname,  b.province, b.city, c.name,c.phone,c.address,a.gold_coin, round(a.gold_coin/10,0) as cny ,a.create_time from base_gold_coin as a
left join  base_wechat_user_info as b
on a.wechat_openid = b.openid
left join base_wechat_user as c
on a.wechat_openid = c.wechat_openid
where a.type = 1 order by a.create_time desc;
```

```
create view view_open_amount_statistic as
select a.wechat_openid,c.nickname,c.province, c.city, a.name, a.address, a.birthday, a.phone,b.addr as open_addr ,b.number as open_amount from base_wechat_user as a  left join ( SELECT  wechat_openid,addr, count(*) as number  FROM stb_open_info where status = 1 group by wechat_openid ) b on a.wechat_openid = b.wechat_openid
left join base_wechat_user_info as c
on a.wechat_openid = c.openid

where number > 0 order by number desc;
```
