package com.swacorp.ais.cdsSalesEventFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import xmlUtil.XmlFormatter;
import xmlUtil.XmlToDoc;

public class CdsSalesEventMessage {
	private XmlFormatter _xmlFormatter = new XmlFormatter();
	private XmlToDoc _xmlToDoc = new XmlToDoc();
	private XPathFactory xpathFactory = XPathFactory.newInstance();
	private XPath xpath = xpathFactory.newXPath();
	private Document _document;
	private String _salesEventText;

	public CdsSalesEventMessage(String salesEventText) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		_salesEventText = salesEventText;
		_document = _xmlToDoc.convert(salesEventText);
	}
	
	public String getFormattedText() throws UnsupportedEncodingException, TransformerFactoryConfigurationError, SAXException, IOException, ParserConfigurationException, TransformerException {
		return _xmlFormatter.formatXml(_salesEventText);
	}
	public String getEventType() throws XPathExpressionException {
		return xpath.evaluate("/CdsEvent/eventHeader/eventType", _document);
	}
	
	public String getTicketNumber() throws XPathExpressionException {
		return xpath.evaluate("/CdsEvent/eventBody/SalesList/Sales/ticketNumber", _document);
	}
	
	public String getDeploymentActivity() throws XPathExpressionException {
	   return xpath.evaluate("/CdsEvent/eventHeader/deploymentActivity", _document);
	}
	public String getRecordLocatorId() throws XPathExpressionException {
		return xpath.evaluate("/CdsEvent/eventBody/SalesList/Sales/recordLocatorID", _document);
	}
	
	public int getTax() throws XPathExpressionException {
	   String xoTaxCount = xpath.evaluate("count(/CdsEvent/eventBody/SalesList/Sales/documentFare/documentTaxList/tax[typeCode='XO'])", _document); 
	   int xoTaxCountNum = Integer.parseInt(xoTaxCount);
	   String xoUsedTaxCount = xpath.evaluate("count(/CdsEvent/eventBody/SalesList/Sales/documentRefundFare/documentTaxList/tax[typeCode='XO'])", _document);
	   int xoUsedTaxCountNum = Integer.parseInt(xoUsedTaxCount);
	   return xoTaxCountNum + xoUsedTaxCountNum; 
	}
	
	public String getCouponRemark() throws XPathExpressionException {
		NodeList coupons = (NodeList) xpath.evaluate("/CdsEvent/eventBody/SalesList/Sales/couponList", _document, XPathConstants.NODESET);
		Element coupon = (Element) coupons.item(0);
		NodeList couponRemarkList = (NodeList)coupon.getElementsByTagName("couponRemarkList");
		Node remark = couponRemarkList.item(0);
		return remark.getNodeValue();
	}
}
