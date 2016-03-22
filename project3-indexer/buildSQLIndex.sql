# use select x(Location) from Item_Location for latitude, y(Location) for longitude
create table Item_Location(
	ItemID int not null,
	Location point not null,
	spatial index (Location),
	primary key (ItemID),
	foreign key (ItemID) references Item(ItemID)
) engine = myisam;

insert into Item_Location (ItemID, Location)
select ItemID, point(Latitude, Longitude)
from Item
where Latitude is not null and Longitude is not null;
