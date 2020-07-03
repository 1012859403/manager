package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.TAdmin;
import com.atguigu.atcrowdfunding.bean.TMenu;
import com.atguigu.atcrowdfunding.service.AdminService;
import com.atguigu.atcrowdfunding.service.MenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    MenuService menuService;
    @RequestMapping("/main.html")
    public String toMainPage(HttpSession session){
        List<TMenu> pMenus = menuService.getPMenu();
        System.out.println("pMenus = " + pMenus);
        session.setAttribute("pMenus",pMenus);
        return "admins/main";
    }
    @Autowired
    AdminService adminService;
//    @RequestMapping("/doLogin")
//    public String doLogin(Model model, HttpSession session, String loginacct, String userpswd){
//        TAdmin admin = adminService.doLogin(loginacct, userpswd);
//        if(admin!=null){
//            session.setAttribute("admin",admin);
//            return "redirect:/admin/main.html";
//        }
//        model.addAttribute("errorMsg","用户名或者密码错误");
//        return "login";
//    }
    //    注销
    @GetMapping("/logOut")
    public String logOut(HttpSession session){
        session.invalidate();
        return "redirect:/toIndex";
    }
    //    得到所有的管理员
    @GetMapping("/index")
    public String getAllAdmins(HttpSession session,@RequestParam(required = false,defaultValue ="") String condition,Model model,@RequestParam(required = false,defaultValue = "1") int pageNum){
//        开始分页,参数1是第几页，参数2是每页几个数据
        PageHelper.startPage(pageNum,3);
        List<TAdmin> admins = adminService.getAllAdmins(condition);
//        获得更加详情的分页数据，参数1是查询的数据的集合，数据2是分页导航栏的页码数
        PageInfo<TAdmin> pageInfo = new PageInfo<TAdmin>(admins,3);
        int pages = pageInfo.getPages();
        session.setAttribute("pages",pages);
//将详情数据存到域中
        model.addAttribute("pageInfo",pageInfo);
        return "admins/user";
    }
    //    删除管理员
    @RequestMapping("/deleteAdminById")
    public String deleteAdminById(Integer id, @RequestHeader("referer") String referer){
        adminService.deleteAdminById(id);
//        全路径只能使用重定向
        return "redirect:"+referer;
    }
    //    转发到新增页面
    @RequestMapping("/add.html")
    public String toAddPage(){
        return "admins/add";
    }
    //    新增管理员
    @PostMapping("/addAdmin")
    public String addAdmin(Model model,TAdmin tAdmin,HttpSession session) {
        try {
            adminService.addAdmin(tAdmin);
            Integer pages = (Integer) session.getAttribute("pages");
            return "redirect:/admin/index?pageNum=" + (pages + 1);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMsg", e.getMessage());
            return "admins/add";
        }

    }
    //    批量删除
    @PreAuthorize("hasAnyAuthority('user:delete')and hasAnyRole('PM - 项目经理')")
    @GetMapping("/batchDeleteAdmins")
    public String batchDeleteAdmins(@RequestHeader("referer") String referer,@RequestParam("ids") List<Integer> ids){
        adminService.batchDeleteAdmin(ids);
        return "redirect:" + referer;
    }
    //    跳转到更新页面
    @GetMapping("/edit.html")
    public String toEditPage(HttpSession session,@RequestHeader("referer") String referer, Model model,Integer id){
        TAdmin admin = adminService.getAdminById(id);
        session.setAttribute("referer",referer);
        model.addAttribute("admin",admin);
        return "admins/edit";
    }
    //    更新管理员
    @PostMapping("/updateAdminById")
    public String updateAdminById(HttpSession session,TAdmin tAdmin){
        adminService.updateAdminById(tAdmin);
        String referer = (String) session.getAttribute("referer");
        session.removeAttribute("referer");
        return "redirect:" + referer;
    }
}
