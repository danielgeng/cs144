select count(*) from User;

select count(i.ItemID) 
from Item i, User u 
where u.UserID = i.Seller and binary u.Location = "New York";

select count(*)
from (select ItemID
	from Item_Category
	group by ItemID
	having count(Category) = 4) c;

select b.ItemID
from Bid b
inner join Item i
on b.ItemID = i.ItemID
where Ends > "2001-12-20 00:00:01"
and Amount = (select max(Amount) from Bid);

select count(*)
from User
where Rating > 1000
and UserID in (select Seller from Item);

select count(distinct i.Seller)
from Item i
inner join Bid b
on i.Seller = b.UserID;

select count(distinct Category)
from Item_Category
where ItemID in (select ItemID
				from Bid
				where Amount > 100.00);
