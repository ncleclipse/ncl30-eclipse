package br.ufma.deinf.laws.ncleclipse.ncl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class NCLParser {
		private ErrorHandler errorHandler;
		private NCLContentHandler contentHandler;

		public void setErrorHandler(ErrorHandler errorHandler)
		{
			this.errorHandler = errorHandler;
		}

		public void setContentHandler(NCLContentHandler contentHandler)
		{
			this.contentHandler = contentHandler;
		}

		public static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";

		/**
		 * Does DTD-based validation on File
		 */
		public void doParse(File xmlFilePath) throws RuntimeException
		{

			InputSource inputSource = null;
			try
			{
				inputSource = new InputSource(new FileReader(xmlFilePath));
			}
			catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			}
			doParse(inputSource);

		}

		/**
		 * Does DTD-based validation on text
		 */
		public void doParse(String xmlText) throws RuntimeException
		{

			InputSource inputSource = new InputSource(new StringReader(xmlText));
			doParse(inputSource);

		}

		/**
		 * Does DTD-based validation on inputSource
		 */
		public void doParse(InputSource inputSource) throws RuntimeException
		{

			try
			{
				XMLReader reader = new SAXParser();
				reader.setErrorHandler(errorHandler);
				reader.setContentHandler(contentHandler);
				//reader.setFeature(VALIDATION_FEATURE, true);
				reader.parse(inputSource);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

}
