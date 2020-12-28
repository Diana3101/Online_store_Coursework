package com.kapatsyn.coursework.controller;

import com.kapatsyn.coursework.dto.OrderDTO;
import com.kapatsyn.coursework.dto.ThingDTO;
import com.kapatsyn.coursework.entity.Thing;
import com.kapatsyn.coursework.form.ThingForm;
import com.kapatsyn.coursework.model.OrderDetailInfo;
import com.kapatsyn.coursework.model.OrderInfo;
import com.kapatsyn.coursework.pagination.PaginationResult;
import com.kapatsyn.coursework.validator.ThingFormValidator;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Transactional
public class AdminController {

    @Autowired
    private OrderDTO orderDTO;

    @Autowired
    private ThingDTO thingDTO;

    @Autowired
    private ThingFormValidator thingFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == ThingForm.class) {
            dataBinder.setValidator(thingFormValidator);
        }
    }

    // GET: Show Login Page
    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
    public String login(Model model) {

        return "login";
    }

    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
    public String accountInfo(Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());

        model.addAttribute("userDetails", userDetails);
        return "index";
    }

    @RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
    public String orderList(Model model, //
                            @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;

        PaginationResult<OrderInfo> paginationResult //
                = orderDTO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);

        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }

    // GET: Show product.
    @RequestMapping(value = { "/admin/thing" }, method = RequestMethod.GET)
    public String thing(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        ThingForm thingForm = null;

        if (code != null && code.length() > 0) {
            Thing thing = thingDTO.findThing(code);
            if (thing != null) {
                thingForm = new ThingForm(thing);
            }
        }
        if (thingForm == null) {
            thingForm = new ThingForm();
            thingForm.setNewThing(true);
        }
        model.addAttribute("thingForm", thingForm);
        return "thing";
    }

    // POST: Save product
    @RequestMapping(value = { "/admin/thing" }, method = RequestMethod.POST)
    public String thingSave(Model model, //
                            @ModelAttribute("thingForm") @Validated ThingForm thingForm, //
                            BindingResult result, //
                            final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "thing";
        }
        try {
            thingDTO.save(thingForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            // Show thing form.
            return "thing";
        }

        return "redirect:/thingList";
    }

    @RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDTO.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        List<OrderDetailInfo> details = this.orderDTO.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);

        model.addAttribute("orderInfo", orderInfo);

        return "order";
    }
}
