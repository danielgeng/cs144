create table User(
	UserID varchar(100) not null,
	Rating int not null,
	Location varchar(100) default null,
	Country varchar(50) default null,
	primary key (UserID)
) engine = innodb;

create table Item(
	ItemID int not null,
	Name varchar(100) not null,
	Currently decimal(8,2) not null,
	Buy_Price decimal(8,2) default null,
	First_Bid decimal(8,2),
	Number_of_Bids int,
	Started timestamp not null,
	Ends timestamp not null,
	Seller varchar(100) not null,
	Description varchar(4000),
	primary key (ItemID),
	foreign key (Seller) references User(UserID)
) engine = innodb;

create table Item_Category(
	ItemID int not null,
	Category varchar(100) not null,
	foreign key (ItemID) references Item(ItemID)
) engine = innodb;

create table Bid(
	ItemID int not null,
	UserID varchar(100) not null,
	Time timestamp not null,
	Amount decimal(8,2) not null,
	primary key (ItemID, UserID, Time),
	foreign key (ItemID) references Item(ItemID),
	foreign key (UserID) references User(UserID)
) engine = innodb;
