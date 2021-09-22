package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
public class Rest {

    private final int CINEMA_ROWS = 9;
    private final int CINEMA_COLUMNS = 9;
    CinemaRoom cinemaRoom = new CinemaRoom(CINEMA_ROWS, CINEMA_COLUMNS);

    Map<UUID, Seat> soldSeats = new HashMap<>();
    private int income = 0;

    @GetMapping("/seats")
    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat wantedSeat) {
        if (!(1 <= wantedSeat.getRow() && wantedSeat.getRow() <= CINEMA_ROWS)
                || !(1 <= wantedSeat.getColumn() && wantedSeat.getColumn() <= CINEMA_COLUMNS)) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "The number of a row or a column is out of bounds!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        boolean available = false;
        Seat foundSeat = null;
        for (Seat seat : cinemaRoom.available_seats) {
            if (seat.getRow() == wantedSeat.getRow() && seat.getColumn() == wantedSeat.getColumn()) {
                available = true;
                foundSeat = seat;
            }
        }
        if (!available) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "The ticket has been already purchased!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        Map<String, Integer> ticket = new HashMap<>();
        ticket.put("row", foundSeat.getRow());
        ticket.put("column", foundSeat.getColumn());
        ticket.put("price", foundSeat.getPrice());
        cinemaRoom.removeSeat(foundSeat);
        UUID generatedToken = UUID.randomUUID();
        soldSeats.put(generatedToken, foundSeat);
        income += foundSeat.getPrice();

        Map<String, Object> result = new HashMap<>();
        result.put("token", generatedToken);
        result.put("ticket", ticket);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnSeat(@RequestBody Map<String, UUID> requestBody) {
        UUID requestToken = requestBody.get("token");
        if (!soldSeats.containsKey(requestToken)) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "Wrong token!");
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        Seat returningSeat = soldSeats.get(requestToken);
        soldSeats.remove(requestToken);
        cinemaRoom.available_seats.add(returningSeat);
        income -= returningSeat.getPrice();
        Map<String, Seat> result = new HashMap<>();
        result.put("returned_ticket", returningSeat);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) String password) {
        if (!Objects.equals("super_secret", password)) {
            Map<String, String> result = new HashMap<>();
            result.put("error", "The password is wrong!");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("current_income", income);
        result.put("number_of_available_seats", cinemaRoom.available_seats.size());
        result.put("number_of_purchased_tickets", soldSeats.size());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
