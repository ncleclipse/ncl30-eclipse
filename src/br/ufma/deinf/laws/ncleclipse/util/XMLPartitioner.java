/*
 * Created on Oct 10, 2004
 */
package br.ufma.deinf.laws.ncleclipse.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * Simple extension of DefaultPartitioner with printPartitions() method to assist with printing out partition
 * information
 * 
 * @author Phil Zoio
 */
public class XMLPartitioner extends FastPartitioner
{

	public XMLPartitioner(IPartitionTokenScanner scanner, String[] legalContentTypes)
	{
		super(scanner, legalContentTypes);
	}

	public ITypedRegion[] computePartitioning(int offset, int length, boolean includeZeroLengthPartitions)
	{
		return super.computePartitioning(offset, length, includeZeroLengthPartitions);
	}

	public void connect(IDocument document, boolean delayInitialization)
	{
		super.connect(document, delayInitialization);
		printPartitions(document);
	}

	public void printPartitions(IDocument document)
	{
		StringBuffer buffer = new StringBuffer();

		ITypedRegion[] partitions = computePartitioning(0, document.getLength());
		for (int i = 0; i < partitions.length; i++)
		{
			try
			{
				buffer.append("Partition type: " + partitions[i].getType() + ", offset: " + partitions[i].getOffset()
						+ ", length: " + partitions[i].getLength());
				buffer.append("\n");
				buffer.append("Text:\n");
				buffer.append(document.get(partitions[i].getOffset(), partitions[i].getLength()));
				buffer.append("\n---------------------------\n\n\n");
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}
		System.out.print(buffer);
	}
}