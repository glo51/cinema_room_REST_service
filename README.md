# cinema_room_REST_service
Create cinema room, sell/return tickets via REST

A room consists of Seat objects (row, column, price).
Create a room with n rows and m columns.
With use of Spring and REST, send proper json body request to:

buy a ticket {"row": 8, "column": 14}

return the ticket {"token": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"}

show cinema statistics (with placeholder password "super_secret") {"password": "super_secret"}
