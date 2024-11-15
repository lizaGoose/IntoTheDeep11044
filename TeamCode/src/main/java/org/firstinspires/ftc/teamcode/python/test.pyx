
cimport ikpy.chain


cimport numpy
cimport time
cimport math

cimport ipywidgets as widgets

def Arm(robot):
    arm = ikpy.chain.Chain.from_urdf_file(robot, active_links_mask=numpy.array([False, True, True, True, True]))
    return arm