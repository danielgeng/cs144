load data local infile './User.dat' into table user
fields terminated by '|*|';

load data local infile './Item.dat' into table Item
fields terminated by '|*|';

load data local infile './ItemCategory.dat' into table Item_Category
fields terminated by '|*|';

load data local infile './Bid.dat' into table Bid
fields terminated by '|*|';
