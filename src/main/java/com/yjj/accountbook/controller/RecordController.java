package com.yjj.accountbook.controller;

import com.yjj.accountbook.common.LoginInterceptor;
import com.yjj.accountbook.common.Result;
import com.yjj.accountbook.entity.AccountRecord;
import com.yjj.accountbook.entity.dto.PageResult;
import com.yjj.accountbook.entity.dto.RecordCreateRequest;
import com.yjj.accountbook.entity.dto.RecordStatistics;
import com.yjj.accountbook.mapper.RecordMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {
    private final RecordMapper recordMapper;
    private final LoginInterceptor loginInterceptor;

    public RecordController(RecordMapper recordMapper, LoginInterceptor loginInterceptor){this.recordMapper=recordMapper;
        this.loginInterceptor = loginInterceptor;
    }
@GetMapping("/{id}")
    public Result<AccountRecord> findById(@PathVariable Integer id,HttpServletRequest request){
    Integer userId=(Integer)request.getAttribute(LoginInterceptor.ATTR_USER_ID);
        AccountRecord s=recordMapper.findById(id,userId);
        if(s==null){
           return Result.fail(404,"没有该账单");
        }
        return Result.ok(s);
}
@GetMapping
    public Result<List<AccountRecord>> findAll(HttpServletRequest request){
        Integer userId=(Integer)request.getAttribute(LoginInterceptor.ATTR_USER_ID);
        return Result.ok(recordMapper.findAll(userId));
}
@PostMapping
    public Result<AccountRecord> add(@Valid @RequestBody RecordCreateRequest req,HttpServletRequest request){
AccountRecord record =new AccountRecord();
    record.setAmount(req.getAmount());
    record.setType(req.getType());
    record.setCategory(req.getCategory());
    record.setNote(req.getNote());
    record.setRecordDate(req.getRecordDate());
    record.setUserId((Integer)request.getAttribute(LoginInterceptor.ATTR_USER_ID));
    recordMapper.insert(record);
return Result.ok(record);
}
@DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,HttpServletRequest request ){
    Integer userId=(Integer)request.getAttribute(LoginInterceptor.ATTR_USER_ID);
        int s=recordMapper.deleteById(id,userId);
        if(s==0){
            return Result.fail(404,"删除失败");
        }
        return Result.ok(null);
}
@GetMapping("/statistics")
    public Result<RecordStatistics> statistics(@RequestParam String month,HttpServletRequest request){
        YearMonth ym=YearMonth.parse(month);
        LocalDate start=ym.atDay(1);
        LocalDate end=ym.plusMonths(1).atDay(1);
        return Result.ok(recordMapper. statistics(start,end,(Integer)request.getAttribute(LoginInterceptor.ATTR_USER_ID)));
}
@GetMapping("/page")
    public Result<PageResult<AccountRecord>>page(
            @RequestParam(required = false)LocalDate start,
            @RequestParam(required = false) LocalDate end,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
             HttpServletRequest request){
    if (page < 1) page = 1;
    if (size < 1) size = 10;
    if (size > 100) size = 100;
    Integer userId=(Integer)request.getAttribute(LoginInterceptor.ATTR_USER_ID);
    long total = recordMapper.countPage(start, end ,type,userId, category);
    List<AccountRecord> list = recordMapper.findPage(start, end, type, category, userId,(page - 1) * size, size);
    return Result.ok(new PageResult<>(total, list));
}

}
