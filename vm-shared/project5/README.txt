1. (4)->(5), (5)->(6)

2. The Buy_Price is stored on the session along with the rest of the item's relevant information. The session is stored on the server,
so a buyer cannot modify any of the item information on the client-side. If a new item is loaded, the session is overwritten with the new
information, so the Buy_Price can never be changed for any item.
