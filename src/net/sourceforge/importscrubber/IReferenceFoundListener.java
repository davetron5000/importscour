package net.sourceforge.importscrubber;/**
 * Defines the behavior of something that wants to be notified when
 * new class references are found
 */public interface IReferenceFoundListener {    public void referenceFound(String className);}