package com.agacorporation.demo.component;

import com.agacorporation.demo.component.commands.RoomFilter;
import com.agacorporation.demo.component.commands.RoomReservationFilter;
import com.agacorporation.demo.domain.Room;
import com.agacorporation.demo.domain.RoomReservation;
import com.agacorporation.demo.domain.User;
import com.agacorporation.demo.service.RoomReservationService;
import com.agacorporation.demo.service.RoomService;
import com.agacorporation.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping
@SessionAttributes({"roomReservation","roomListPage"})
public class RoomReservationFormController {

    private RoomReservationService roomReservationService;

    @Autowired
    RoomService roomService;

    @Autowired
    UserService userService;

    // Constructor dependency injection. Since version 4.3 of Spring, @Autowired is not required if there is only one constructor.
    @Autowired
    public RoomReservationFormController(RoomReservationService roomReservationService) {
        this.roomReservationService = roomReservationService;
    }

    @RequestMapping(value = "/reservationFormUSR.html", method = RequestMethod.GET)
    public String showFormUSR(Model model, Pageable pageable) {
        // Add an empty RoomReservation object for form binding
        model.addAttribute("roomReservation", new RoomReservation());

        // Add a page of rooms for the dropdown (fetch using your RoomService)
        Page<Room> roomListPage = roomService.getAllRooms(new RoomFilter(), pageable);
        model.addAttribute("roomListPage", roomListPage);

        return "reservationFormUSR";
    }


    @RequestMapping(value="/reservationFormADM.html", method= RequestMethod.GET)
    public String showFormADM(Model model, Optional<Long> id){

        model.addAttribute("roomReservation",
                id.isPresent() ?
                        roomReservationService.getRoomReservation(id.get()) :
                        new RoomReservation());

        return "reservationFormADM.html";
    }

    @RequestMapping(value="/editReservationFormADM.html", method= RequestMethod.GET)
    public String showEditFormADM(Model model, Optional<Long> id){

        model.addAttribute("roomReservation",
                id.isPresent() ?
                        roomReservationService.getRoomReservation(id.get()) :
                        new RoomReservation());

        return "editReservationFormADM.html";
    }

    @RequestMapping(value="/availableRooms.html", method= RequestMethod.GET)
    public String showFormUSR2(Model model, @ModelAttribute("roomReservation") RoomReservation v, @ModelAttribute("roomListPage") Page<Room> r){

        model.addAttribute("roomListPage", r);
        model.addAttribute("roomReservation", v);

        return "availableRooms.html";
    }

    @RequestMapping(value = "/reservationFormUSR.html", method = RequestMethod.POST)
    public String processFormUSR(
            @Valid @ModelAttribute("roomReservation") RoomReservation roomReservation,
            BindingResult bindingResult,
            Model model,
            Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            // Re-populate the room list if there are validation errors
            model.addAttribute("roomListPage", roomService.getAllRooms(new RoomFilter(), pageable));
            return "reservationFormUSR";
        }

        // Save the reservation (adjust as needed)
        roomReservationService.saveRoomReservation(roomReservation);

        return "redirect:/availableRooms.html";
    }


    @RequestMapping(value="/reservationFormADM.html", method= RequestMethod.POST)
    public String processFormADM(Model model, @Valid @ModelAttribute("roomReservation") RoomReservation v, Pageable pageable, RoomFilter search, Principal principal, BindingResult errors){

        if(errors.hasErrors()){
            return "reservationFormADM";
        }

        User usertemp = userService.getUserByLogin(v.getUser().getLogin());
        v.setUser(usertemp);
        search.setNumberOfPeople(v.getNumberOfPeople());
        search.setReservationStartDate(v.getReservationStartDate());
        search.setReservationEndDate(v.getReservationEndDate());

        model.addAttribute("roomListPage", roomService.getAllRooms(search, pageable));
        model.addAttribute("roomReservation", v);
        return "redirect:/availableRooms.html";
    }

    @RequestMapping(value="/editReservationFormADM.html", method= RequestMethod.POST)
    public String processEditFormADM(@Valid @ModelAttribute("roomReservation") RoomReservation v, BindingResult errors){

        roomReservationService.saveRoomReservation(v);
        return "redirect:/reservationList.html";
    }

    @RequestMapping(value="/availableRooms.html", method= RequestMethod.POST)
    public String processFormUSR2(@Valid @ModelAttribute("roomReservation") RoomReservation v, @ModelAttribute("roomListPage") Page<Room> r, Principal principal){
        v.setPaid(false);
        v.setVerified(false);
        roomReservationService.saveRoomReservation(v);

        Long idRoom = v.getRoom().getId();
        Room rr = roomService.getRoom(idRoom);

        Set<RoomReservation> roomReservations = rr.getRoomReservations();
        roomReservations.add(v);
        rr.setRoomReservations(roomReservations);

        if(principal.getName().equals("admin")){
            return "redirect:reservationList.html";
        }
        else {
            return "redirect:yourReservationList.html";
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, false);
        binder.registerCustomEditor(Date.class, "reservationStartDate", dateEditor);
        binder.registerCustomEditor(Date.class, "reservationEndDate", dateEditor);
        binder.registerCustomEditor(Date.class, "checkInDate", dateEditor);
        binder.registerCustomEditor(Date.class, "checkOutDate", dateEditor);

        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
        binder.registerCustomEditor(Float.class, "price", new CustomNumberEditor(Float.class, numberFormat, false));
    }
}
