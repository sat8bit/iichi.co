package co.iichi.web.controller;

import co.iichi.common.yjdn.shopping.itemsearch.ItemSearchClient;
import co.iichi.common.yjdn.shopping.itemsearch.response.Hit;
import co.iichi.common.yjdn.shopping.itemsearch.response.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "imp")
public class ImpController {
    @Autowired
    protected ItemSearchClient itemSearchClient;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getIndex(
            @RequestParam(name = "needle") Optional<String> needle,
            @RequestParam(name = "page") Optional<Integer> page
    ) {

        ModelAndView modelAndView =  new ModelAndView("imp/index");

        Integer pageInt = page.orElse(0);

        if (Integer.MAX_VALUE / ItemSearchClient.LIMIT < pageInt) {
            pageInt = 0;
        }

        if (needle.isPresent()) {
            Optional<ResultSet> resultSet
                    = itemSearchClient.searchByQuery(needle.get(), pageInt * ItemSearchClient.LIMIT);

            if (resultSet.isPresent()) {
                List<Hit> hitList = resultSet.get()
                        .getResult()
                        .getHitList()
                        .stream()
                        .collect(Collectors.toList());

                modelAndView.addObject("hitList", hitList);
                modelAndView.addObject("page", pageInt);
                modelAndView.addObject("needle", needle.get());
            }
        }

        return modelAndView;
    }

}
