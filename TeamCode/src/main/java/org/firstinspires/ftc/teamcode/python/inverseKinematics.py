%%cython
import ikpy.chain


import numpy
import time
import math

import ipywidgets as widgets

cdef Arm(robot):
    arm = ikpy.chain.Chain.from_urdf_file(robot, active_links_mask=numpy.array([False, True, True, True, True]))
    return arm
