//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.12.05 at 12:04:53 PM GST 
//


package com.maintainapps.module.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for singleFunctionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="singleFunctionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://bayan.ir}functions"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "singleFunctionType", propOrder = {
    "expr",
    "constant",
    "concat",
    "replace",
    "trim",
    "truncate",
    "text",
    "raw",
    "attribute",
    "first",
    "last",
    "size",
    "url",
    "resolve",
    "link",
    "forEach",
    "fetch",
    "fieldValue",
    "decode",
    "_default",
    "process"
})
@XmlSeeAlso({
    RawType.class,
    TruncateType.class,
    ExtractToType.class,
    AttributeType.class,
    ProcessType.class,
    TrimType.class,
    SizeType.class,
    ResolveType.class,
    FirstType.class,
    ReplaceType.class,
    TextType.class,
    ForEachType.class,
    LastType.class,
    DecodeType.class
})
public class SingleFunctionType {

    protected ExprType expr;
    protected ConstantType constant;
    protected ConcatType concat;
    protected ReplaceType replace;
    protected TrimType trim;
    protected TruncateType truncate;
    protected TextType text;
    protected RawType raw;
    protected AttributeType attribute;
    protected FirstType first;
    protected LastType last;
    protected SizeType size;
    protected UrlType url;
    protected ResolveType resolve;
    protected LinkType link;
    @XmlElement(name = "for-each")
    protected ForEachType forEach;
    protected FetchType fetch;
    @XmlElement(name = "field-value")
    protected FieldValueType fieldValue;
    protected DecodeType decode;
    @XmlElement(name = "default")
    protected DefaultType _default;
    protected ProcessType process;

    /**
     * Gets the value of the expr property.
     * 
     * @return
     *     possible object is
     *     {@link ExprType }
     *     
     */
    public ExprType getExpr() {
        return expr;
    }

    /**
     * Sets the value of the expr property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExprType }
     *     
     */
    public void setExpr(ExprType value) {
        this.expr = value;
    }

    /**
     * Gets the value of the constant property.
     * 
     * @return
     *     possible object is
     *     {@link ConstantType }
     *     
     */
    public ConstantType getConstant() {
        return constant;
    }

    /**
     * Sets the value of the constant property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConstantType }
     *     
     */
    public void setConstant(ConstantType value) {
        this.constant = value;
    }

    /**
     * Gets the value of the concat property.
     * 
     * @return
     *     possible object is
     *     {@link ConcatType }
     *     
     */
    public ConcatType getConcat() {
        return concat;
    }

    /**
     * Sets the value of the concat property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConcatType }
     *     
     */
    public void setConcat(ConcatType value) {
        this.concat = value;
    }

    /**
     * Gets the value of the replace property.
     * 
     * @return
     *     possible object is
     *     {@link ReplaceType }
     *     
     */
    public ReplaceType getReplace() {
        return replace;
    }

    /**
     * Sets the value of the replace property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReplaceType }
     *     
     */
    public void setReplace(ReplaceType value) {
        this.replace = value;
    }

    /**
     * Gets the value of the trim property.
     * 
     * @return
     *     possible object is
     *     {@link TrimType }
     *     
     */
    public TrimType getTrim() {
        return trim;
    }

    /**
     * Sets the value of the trim property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrimType }
     *     
     */
    public void setTrim(TrimType value) {
        this.trim = value;
    }

    /**
     * Gets the value of the truncate property.
     * 
     * @return
     *     possible object is
     *     {@link TruncateType }
     *     
     */
    public TruncateType getTruncate() {
        return truncate;
    }

    /**
     * Sets the value of the truncate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TruncateType }
     *     
     */
    public void setTruncate(TruncateType value) {
        this.truncate = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setText(TextType value) {
        this.text = value;
    }

    /**
     * Gets the value of the raw property.
     * 
     * @return
     *     possible object is
     *     {@link RawType }
     *     
     */
    public RawType getRaw() {
        return raw;
    }

    /**
     * Sets the value of the raw property.
     * 
     * @param value
     *     allowed object is
     *     {@link RawType }
     *     
     */
    public void setRaw(RawType value) {
        this.raw = value;
    }

    /**
     * Gets the value of the attribute property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeType }
     *     
     */
    public AttributeType getAttribute() {
        return attribute;
    }

    /**
     * Sets the value of the attribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeType }
     *     
     */
    public void setAttribute(AttributeType value) {
        this.attribute = value;
    }

    /**
     * Gets the value of the first property.
     * 
     * @return
     *     possible object is
     *     {@link FirstType }
     *     
     */
    public FirstType getFirst() {
        return first;
    }

    /**
     * Sets the value of the first property.
     * 
     * @param value
     *     allowed object is
     *     {@link FirstType }
     *     
     */
    public void setFirst(FirstType value) {
        this.first = value;
    }

    /**
     * Gets the value of the last property.
     * 
     * @return
     *     possible object is
     *     {@link LastType }
     *     
     */
    public LastType getLast() {
        return last;
    }

    /**
     * Sets the value of the last property.
     * 
     * @param value
     *     allowed object is
     *     {@link LastType }
     *     
     */
    public void setLast(LastType value) {
        this.last = value;
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link SizeType }
     *     
     */
    public SizeType getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link SizeType }
     *     
     */
    public void setSize(SizeType value) {
        this.size = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link UrlType }
     *     
     */
    public UrlType getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link UrlType }
     *     
     */
    public void setUrl(UrlType value) {
        this.url = value;
    }

    /**
     * Gets the value of the resolve property.
     * 
     * @return
     *     possible object is
     *     {@link ResolveType }
     *     
     */
    public ResolveType getResolve() {
        return resolve;
    }

    /**
     * Sets the value of the resolve property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResolveType }
     *     
     */
    public void setResolve(ResolveType value) {
        this.resolve = value;
    }

    /**
     * Gets the value of the link property.
     * 
     * @return
     *     possible object is
     *     {@link LinkType }
     *     
     */
    public LinkType getLink() {
        return link;
    }

    /**
     * Sets the value of the link property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinkType }
     *     
     */
    public void setLink(LinkType value) {
        this.link = value;
    }

    /**
     * Gets the value of the forEach property.
     * 
     * @return
     *     possible object is
     *     {@link ForEachType }
     *     
     */
    public ForEachType getForEach() {
        return forEach;
    }

    /**
     * Sets the value of the forEach property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForEachType }
     *     
     */
    public void setForEach(ForEachType value) {
        this.forEach = value;
    }

    /**
     * Gets the value of the fetch property.
     * 
     * @return
     *     possible object is
     *     {@link FetchType }
     *     
     */
    public FetchType getFetch() {
        return fetch;
    }

    /**
     * Sets the value of the fetch property.
     * 
     * @param value
     *     allowed object is
     *     {@link FetchType }
     *     
     */
    public void setFetch(FetchType value) {
        this.fetch = value;
    }

    /**
     * Gets the value of the fieldValue property.
     * 
     * @return
     *     possible object is
     *     {@link FieldValueType }
     *     
     */
    public FieldValueType getFieldValue() {
        return fieldValue;
    }

    /**
     * Sets the value of the fieldValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldValueType }
     *     
     */
    public void setFieldValue(FieldValueType value) {
        this.fieldValue = value;
    }

    /**
     * Gets the value of the decode property.
     * 
     * @return
     *     possible object is
     *     {@link DecodeType }
     *     
     */
    public DecodeType getDecode() {
        return decode;
    }

    /**
     * Sets the value of the decode property.
     * 
     * @param value
     *     allowed object is
     *     {@link DecodeType }
     *     
     */
    public void setDecode(DecodeType value) {
        this.decode = value;
    }

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultType }
     *     
     */
    public DefaultType getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultType }
     *     
     */
    public void setDefault(DefaultType value) {
        this._default = value;
    }

    /**
     * Gets the value of the process property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessType }
     *     
     */
    public ProcessType getProcess() {
        return process;
    }

    /**
     * Sets the value of the process property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessType }
     *     
     */
    public void setProcess(ProcessType value) {
        this.process = value;
    }

}
