package com.arisux.airi.coremod;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;
import scala.tools.asm.ClassReader;
import scala.tools.asm.ClassWriter;
import scala.tools.asm.tree.AbstractInsnNode;
import scala.tools.asm.tree.ClassNode;
import scala.tools.asm.tree.LineNumberNode;
import scala.tools.asm.tree.MethodNode;

import com.arisux.airi.AIRI;
import com.arisux.airi.lib.ModLib;

public class ASMTransformer implements IClassTransformer
{
	public ASMTransformer() throws Exception
	{
		AIRI.logger.info("ASM: %s", this.getClass().getName());
	}

	@Override
	public byte[] transform(String className, String arg1, byte[] bytes)
	{
		if (className.equals("net.minecraft.client.renderer.EntityRenderer"))
		{
			return this.patchClassASM(className, ModLib.isDevelopmentEnvironment() ? "updateLightmap" : "func_78472_g", "(F)V", bytes);
		}

		return bytes;
	}

	public byte[] patchClassASM(String className, String targetMethodName, String targetMethodSignature, byte[] bytes)
	{
		ClassNode classNode = new ClassNode();
		ClassReader reader = new ClassReader(bytes);

		reader.accept(classNode, 0);
		Iterator<MethodNode> methods = classNode.methods.iterator();
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

		while (methods.hasNext())
		{
			MethodNode mv = methods.next();

			if ((mv.name.equals(targetMethodName) && mv.desc.equals(targetMethodSignature)))
			{
				AIRI.logger.info("********** PATCHING %s %s%s", className, mv.name, mv.desc);

				mv.instructions.iterator();

				int targetIndex = 0;

				for (int i = 0; i < mv.instructions.size(); i++)
				{
					AbstractInsnNode currentNode = mv.instructions.get(i);

					if (currentNode instanceof LineNumberNode)
					{
						if (((LineNumberNode) currentNode).line == 953)
						{
							targetIndex = i;
							AIRI.logger.info("********** INDEX - %s:%s", 953, targetIndex);
						}
					}
				}

				mv.instructions.remove(mv.instructions.get(targetIndex + 0));

				AIRI.logger.info("********** PATCH COMPLETE");
				break;
			}
		}

		classNode.accept(writer);
		return writer.toByteArray();
	}
}