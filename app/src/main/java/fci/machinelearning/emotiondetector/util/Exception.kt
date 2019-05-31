package fci.machinelearning.emotiondetector.util

/**
 * @author Ahmad Khalifa
 */

class NoInternetConnectionException : RuntimeException("Unable to connect to the internet")

class FragmentNotAttachedException : IllegalStateException("Fragment is not attached to activity")