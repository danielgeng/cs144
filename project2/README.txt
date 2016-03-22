Part B: Relational schema design

1. List of relations

Item(ItemID, Name, Currently, Buy_Price, First_Bid, Number_of_Bids, Started, Ends, Seller, Description) Key: ItemID

User(UserID, Rating, Location, Country) Key: UserID

Item_Category(ItemID, Category)

Bid(ItemID, UserID, Time, Amount) Key: (ItemID, UserID, Time)

2. The functional dependencies are stated in the keys of each relation.

3. Yes, all relations are in BCNF.

4. Yes, all relations are in 4NF. 
