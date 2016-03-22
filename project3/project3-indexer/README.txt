Part A: Lucene Indexes

1. ItemID (StringField)

The ItemID is stored as StringField because a search query should only return items that match the entire ID.

2. Name (TextField)
3. Category (TextField)
4. Description (TextField)

The name, category, and description of items are stored as TextField because a search query should be able to return items that contain any word of the query.
