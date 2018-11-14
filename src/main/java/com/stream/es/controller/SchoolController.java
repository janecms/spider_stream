package com.stream.es.controller;
import com.stream.es.model.ApiResponse;
import com.stream.es.model.DuHit;
import com.stream.es.model.Hit;
import com.stream.es.model.School;
import com.stream.es.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 *
 *
 */

@RequestMapping("/school")
@Controller
public class SchoolController {
    @Autowired
    SchoolService schoolService;


    @RequestMapping({"", "/"})
    public String list(ModelMap modelMap) {
        final List<String> tags = schoolService.getTags();
        modelMap.put("tags",tags);
        return "/school/school_list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse list(@RequestParam(value = "pageNum", defaultValue = "1") int page, int pageSize, School school) {
        Hit<School> result = schoolService.query(page,pageSize, school);
        return ApiResponse.success(result.getHints()).setCount(result.getTotal());
    }
    @ResponseBody
    @RequestMapping("/dupex/{schoolId}")
    public ApiResponse dupex(@PathVariable("schoolId") String schoolId){
        final School school = schoolService.get(schoolId);
        final List<DuHit> duHitList = schoolService.duSchool(school, 0.2f);
        List result = new ArrayList();
        duHitList.stream().forEach(hit ->{
            final School data = (School)hit.getData();
            Map<String, String> map =null;
            try {
                map = org.apache.commons.beanutils.BeanUtils.describe(data);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            ;
            map.put("score",String.valueOf(hit.getScore()));
            if (data.getId().equals(schoolId)){
                map.put("self","true");
            }else{
                map.put("self","false");
            }
            result.add(map);
        });
        return ApiResponse.success(result).setCount(result.size());
    }

    @RequestMapping("/listdupex/{schoolId}")
    public String listdupex(@PathVariable("schoolId") String schoolId,ModelMap modelMap){
        modelMap.put("schoolId",schoolId);
        return "/school/school_dupex";
    }
}
