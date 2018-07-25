package com.dxtnerp.model.bs_work;


/**
 * 销售订单申请->产品信息查询
 */
public class ProductInfo {

    /**
     * id_item : 0101170142
     * var_itemhelp : XYS
     * name_item : 西洋参
     * var_spec : 切片80克/瓶×2瓶
     * id_uom : 131
     * name_uom : 盒
     * id_prodarea :
     * name_prodarea : 加拿大
     * dec_qty : 0
     * var_paddr :
     * var_pcom :
     * date_quality :
     * var_approval :
     * var_requirements :
     */

    private String id_item;
    private String var_itemhelp;
    private String name_item;
    private String var_spec;
    private String id_uom;
    private String name_uom;
    private String id_prodarea;
    private String name_prodarea;
    private String dec_qty;
    private String var_paddr;
    private String var_pcom;
    private String date_quality;
    private String var_approval;
    private String var_requirements;
    private boolean isCheck;

    public ProductInfo(String id_item, String var_itemhelp, String name_item, String var_spec, String id_uom, String name_uom, String id_prodarea, String name_prodarea, String dec_qty, String var_paddr, String var_pcom, String date_quality, String var_approval, String var_requirements) {
        this.id_item = id_item;
        this.var_itemhelp = var_itemhelp;
        this.name_item = name_item;
        this.var_spec = var_spec;
        this.id_uom = id_uom;
        this.name_uom = name_uom;
        this.id_prodarea = id_prodarea;
        this.name_prodarea = name_prodarea;
        this.dec_qty = dec_qty;
        this.var_paddr = var_paddr;
        this.var_pcom = var_pcom;
        this.date_quality = date_quality;
        this.var_approval = var_approval;
        this.var_requirements = var_requirements;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getId_item() {
        return id_item;
    }

    public void setId_item(String id_item) {
        this.id_item = id_item;
    }

    public String getVar_itemhelp() {
        return var_itemhelp;
    }

    public void setVar_itemhelp(String var_itemhelp) {
        this.var_itemhelp = var_itemhelp;
    }

    public String getName_item() {
        return name_item;
    }

    public void setName_item(String name_item) {
        this.name_item = name_item;
    }

    public String getVar_spec() {
        return var_spec;
    }

    public void setVar_spec(String var_spec) {
        this.var_spec = var_spec;
    }

    public String getId_uom() {
        return id_uom;
    }

    public void setId_uom(String id_uom) {
        this.id_uom = id_uom;
    }

    public String getName_uom() {
        return name_uom;
    }

    public void setName_uom(String name_uom) {
        this.name_uom = name_uom;
    }

    public String getId_prodarea() {
        return id_prodarea;
    }

    public void setId_prodarea(String id_prodarea) {
        this.id_prodarea = id_prodarea;
    }

    public String getName_prodarea() {
        return name_prodarea;
    }

    public void setName_prodarea(String name_prodarea) {
        this.name_prodarea = name_prodarea;
    }

    public String getDec_qty() {
        return dec_qty;
    }

    public void setDec_qty(String dec_qty) {
        this.dec_qty = dec_qty;
    }

    public String getVar_paddr() {
        return var_paddr;
    }

    public void setVar_paddr(String var_paddr) {
        this.var_paddr = var_paddr;
    }

    public String getVar_pcom() {
        return var_pcom;
    }

    public void setVar_pcom(String var_pcom) {
        this.var_pcom = var_pcom;
    }

    public String getDate_quality() {
        return date_quality;
    }

    public void setDate_quality(String date_quality) {
        this.date_quality = date_quality;
    }

    public String getVar_approval() {
        return var_approval;
    }

    public void setVar_approval(String var_approval) {
        this.var_approval = var_approval;
    }

    public String getVar_requirements() {
        return var_requirements;
    }

    public void setVar_requirements(String var_requirements) {
        this.var_requirements = var_requirements;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "id_item='" + id_item + '\'' +
                ", var_itemhelp='" + var_itemhelp + '\'' +
                ", name_item='" + name_item + '\'' +
                ", var_spec='" + var_spec + '\'' +
                ", id_uom='" + id_uom + '\'' +
                ", name_uom='" + name_uom + '\'' +
                ", id_prodarea='" + id_prodarea + '\'' +
                ", name_prodarea='" + name_prodarea + '\'' +
                ", dec_qty='" + dec_qty + '\'' +
                ", var_paddr='" + var_paddr + '\'' +
                ", var_pcom='" + var_pcom + '\'' +
                ", date_quality='" + date_quality + '\'' +
                ", var_approval='" + var_approval + '\'' +
                ", var_requirements='" + var_requirements + '\'' +
                '}';
    }
}
