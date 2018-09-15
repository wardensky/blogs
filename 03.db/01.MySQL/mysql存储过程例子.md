# mysql存储过程例子

```
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `proc_statistic`(OUT today_number int,out today_binding int, out today_money int, out today_wechat int ,OUT yesterday_number int,out yesterday_binding int, out yesterday_money int, out yesterday_wechat int,OUT total_number int,out total_binding int, out total_money int, out total_wechat int)
BEGIN
set @today = (select date_format(now(),'%Y-%m-%d'));
set @yesterday = (select date_format(date_sub(now(),interval 1 day),'%Y-%m-%d'));
set today_number = (select count(*) from stb_open_info where status = 1 and create_time like concat(@today,'%'));
set today_binding = (select count(*) from hhtd_wechat.base_wechat_binding where create_time like concat(@today,'%'));
set today_money = (select round(sum(gold_coin)/10,0) as money from base_gold_coin where type = 1 and create_time like concat(@today,'%'));
set today_wechat = (select count(*) from base_wechat_user where follow = 1 and create_time like concat(@today,'%'));
set yesterday_number = (select count(*) from stb_open_info where status = 1 and create_time like concat(@yesterday,'%'));
set yesterday_binding = (select count(*) from hhtd_wechat.base_wechat_binding where create_time like concat(@yesterday,'%'));
set yesterday_money = (select round(sum(gold_coin)/10,0) as money from base_gold_coin where type = 1 and create_time like concat(@yesterday,'%'));
set yesterday_wechat = (select count(*) from base_wechat_user where follow = 1 and  create_time like concat(@yesterday,'%'));
set total_number = (select count(*) from stb_open_info where status = 1 );
set total_binding = (select count(*) from hhtd_wechat.base_wechat_binding );
set total_money = (select round(sum(gold_coin)/10,0) as money from base_gold_coin where type = 1);
set total_wechat = (select count(*) from base_wechat_user where follow = 1 );

    END;;
DELIMITER ;

```
