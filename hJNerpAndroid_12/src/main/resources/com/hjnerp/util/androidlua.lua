 
-- json����json
function hju_getjsonvalues(  jsonvalues,field) 
	local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
	local value =stc:jhju_getjsonvalue(jsonvalues,field) 
	return value
end

function hju_getuuid()

    local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
	local value =stc:jhjc_getuuid() 
	return value
end 
 
 --------------------���溯��
---  ȡ�����
function hjv_getparentnode(viewid)
    local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    return  stc:jhjv_getparentnode(viewid)
end  
---
-- ����һ��View�����ݵ�1347
function hjv_savedata(viewid) 
    local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    return  stc:jhjv_savedata(viewid)
end

-- ����һ��View�ĵ��ݺ�
function hjv_setbillno(viewid, billno) 
     local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjv_setbillno(viewid,billno)
end 
--  ���ݿ����  
function hjdb_getvalue(billno,id_node,column)

       local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       return  stc:getdb_nodevalue(billno,id_node,column)
end
 
 --  ���ݿ����
function hjdb_updatevalue(billno,nodeid,field,values)
   local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
   stc:setdb_nodevalue(billno,nodeid,field,values) 
end
  
----------------���ݼ�����
-- ���һ�����ݼ�
function hjdts_additem(viewid,dataset,condition) 
	  local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jdts_additem(viewid,dataset,condition) 
end

-- �޸����ݼ�������
function hjdts_setcondition(viewid, key, condition)
      local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjdts_setcondition(viewid,key,condition) 
end 
    
-- ��������Դ���
function hjds_search(viewid,datasource,condition) 
      local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjds_search(viewid,datasource,condition) 
end 
-- ����ֵ�ӿ� ������Ϊ���� 
function hjc_setvalue(viewid,controlid,values,...) 
       local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       if 0== arg.n then
         stc:jhjc_setvalue(viewid,controlid,values) 
       else
         stc:hjc_setvalue(viewid,controlid,arg[1],arg[2],values) 
       end 
 end   
-- ȡֵ�ӿ�  ������Ϊ���� 
function hjc_getvalue(viewid,controlid,... )
       local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       if 0== arg.n then
         return stc:jhjc_getvalue(viewid,controlid) 
       else
         return  stc:jhjc_getvalue(viewid,controlid,arg[1],arg[2]);
       end  
 end 
 function hjc_additem(viewid,controlid,billno,nodeid,value)
       local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       stc:jhjc_additem( viewid, controlid, billno,nodeid, value)
 end 
--  ȡ��ǰ����� ParentNode 
function hjv_getbillno(viewid) 
    local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    return  stc:jhjv_getbillno(viewid)
end 
 

-- ������һ������
function hjc_setnextview(viewid,controlid, row,viewid2 ,billno,nodeid) 
   local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
   stc:jhjc_setnextview(viewid,controlid, row,viewid2,billno,nodeid,sender.values) 
end

-- ����ǰ�����
function hjc_setbackview(viewid) 
   local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
   stc:jhjc_setbackview(viewid)
end

-- �رս���
function hjc_setfinishview(viewid) 
   local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
   stc:jhjc_setfinishview(viewid)
end

-- ����һ���ؼ������غ���ʾ
function hjc_visible(viewid, controlid, visible)
	 local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:setcontorl_visible(viewid, controlid, visible)
end

--  �����ϴ����ɸ��ڵ㿪ʼ�������ݣ�
function hjc_controlupload(viewid,controlid )
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjc_controluplod(viewid,controlid )
end

--  �����ϴ�(���ӽڵ㿪ʼ��������)
function hjdb_nodeupload(billno )
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjdb_nodeuplod(billno )
end

-- ˢ�¿ؼ�����Դ
function hjc_datarefresh(viewid,controlid,datasource)  
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjc_datarefresh(viewid,controlid,datasource)
end

---��λ
function hjc_getlocation(viewid,controlid)  
    local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    stc:jhjc_Location(viewid, controlid) 
end  
---��������
---function hjc_setphoto(viewid,controlid)
---   local levl =  context:getControl(viewid,controlid)
---   levl:setPhoto()
---end

---չʾ��ǰ����ȫ����Ƭ
function hjc_browsephoto(viewid,billno,nodeid)
	-- context:startShowPictures(billno,nodeid)
	 local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjc_browsephoto(viewid,billno,nodeid); 
end 
----�ؼ�����
function hjc_getrowcount(viewid,controlid)
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     return  stc:jhjc_getrowcount(viewid,controlid);
end  
--- �ݷ�ҵ�� 
 function hjb_setddiscard(viewid,locationid,photoid, nextview)
    local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
     stc:jhjb_setddiscard(viewid,locationid,photoid,nextview, sender.billno,sender.nodeid  )  
 end  
function hju_getuuid()
	 local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     return  stc:jhju_getuuid();
end 
-----������
function hju_sendsms(viewid,phone,sms)
      local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhju_sendsms(viewid,phone,sms); 
end  
-----��ʾ��
function hjc_setmakeText(viewid,maketext)
      local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjc_setmaketext(viewid,maketext);
end  
-----���ÿؼ��Ƿ����
function hjc_setenabled(viewid,controlid,enabled)
      local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjc_setenabled(viewid,controlid,enabled);
end  
